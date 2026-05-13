package com.geoscale.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geoscale.dto.request.MatchAnswerRequest;
import com.geoscale.dto.response.*;
import com.geoscale.entity.*;
import com.geoscale.entity.enums.MatchStatus;
import com.geoscale.exception.BadRequestException;
import com.geoscale.exception.ForbiddenException;
import com.geoscale.exception.ResourceNotFoundException;
import com.geoscale.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchService {

    private static final int TOTAL_QUESTIONS = 10;

    private final MatchRepository matchRepository;
    private final MatchQuestionRepository matchQuestionRepository;
    private final MatchAnswerRepository matchAnswerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final MatchEventPublisher matchEventPublisher;
    private final EloService eloService;

    // ------------------------------------------------------------------ //
    //  CREATE
    // ------------------------------------------------------------------ //

    @Transactional
    public MatchCreateResponse createMatch(Long player1Id) {
        User player1 = fetchUser(player1Id);

        Match match = Match.builder()
                .player1(player1)
                .status(MatchStatus.WAITING)
                .build();

        match = matchRepository.save(match);
        // No WebSocket publish here — no opponent yet

        return MatchCreateResponse.builder()
                .matchId(match.getId())
                .status(match.getStatus())
                .build();
    }

    // ------------------------------------------------------------------ //
    //  JOIN
    // ------------------------------------------------------------------ //

    @Transactional
    public MatchCreateResponse joinMatch(Long matchId, Long player2Id) {
        Match match = fetchMatch(matchId);

        if (match.getStatus() != MatchStatus.WAITING) {
            throw new BadRequestException("Match is not open for joining.");
        }
        if (match.getPlayer1().getId().equals(player2Id)) {
            throw new BadRequestException("You cannot join your own match.");
        }

        User player2 = fetchUser(player2Id);
        match.setPlayer2(player2);
        match.setStatus(MatchStatus.ACTIVE);

        assignQuestions(match);

        matchRepository.save(match);
        matchEventPublisher.publishMatchState(match, "PLAYER_JOINED"); // ✅ correct location

        return MatchCreateResponse.builder()
                .matchId(match.getId())
                .status(match.getStatus())
                .build();
    }

    // ------------------------------------------------------------------ //
    //  GET NEXT QUESTION
    // ------------------------------------------------------------------ //

    @Transactional(readOnly = true)
    public MatchQuestionResponse getNextQuestion(Long matchId, Long userId) {
        Match match = fetchMatch(matchId);
        assertPlayer(match, userId);
        assertActive(match);

        int answeredByMe = matchAnswerRepository.countByMatchIdAndPlayerId(matchId, userId);

        if (answeredByMe >= TOTAL_QUESTIONS) {
            throw new BadRequestException("You have already answered all questions.");
        }

        MatchQuestion mq = matchQuestionRepository
                .findByMatchIdAndQuestionOrder(matchId, answeredByMe)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found at index " + answeredByMe));

        Question q = mq.getQuestion();

        return MatchQuestionResponse.builder()
                .questionIndex(answeredByMe + 1)
                .questionId(q.getId())
                .prompt(q.getPrompt())
                .options(buildOptions(q.getOptionsJson()))
                .points(q.getPoints())
                .totalQuestions(TOTAL_QUESTIONS)
                .build();
    }

    // ------------------------------------------------------------------ //
    //  SUBMIT ANSWER
    // ------------------------------------------------------------------ //

    @Transactional
    public MatchAnswerResponse submitAnswer(Long matchId, Long userId, MatchAnswerRequest req) {
        Match match = fetchMatch(matchId);
        assertPlayer(match, userId);
        assertActive(match);

        User player = fetchUser(userId);
        Question question = questionRepository.findById(req.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + req.getQuestionId()));

        // Validate question belongs to this match
        boolean questionInMatch = matchQuestionRepository
                .findByMatchIdOrderByQuestionOrder(matchId)
                .stream()
                .anyMatch(mq -> mq.getQuestion().getId().equals(req.getQuestionId()));

        if (!questionInMatch) {
            throw new BadRequestException("This question does not belong to the current match.");
        }

        // Prevent duplicate answers
        if (matchAnswerRepository.existsByMatchIdAndPlayerIdAndQuestionId(matchId, userId, req.getQuestionId())) {
            throw new BadRequestException("You have already answered this question.");
        }

        // Prevent answering out of order
        int answeredSoFar = matchAnswerRepository.countByMatchIdAndPlayerId(matchId, userId);
        MatchQuestion expected = matchQuestionRepository
                .findByMatchIdAndQuestionOrder(matchId, answeredSoFar)
                .orElseThrow(() -> new BadRequestException("No more questions available."));

        if (!expected.getQuestion().getId().equals(req.getQuestionId())) {
            throw new BadRequestException("Answer the current question before skipping ahead.");
        }

        // Evaluate
        boolean correct = question.getCorrectAnswer().equalsIgnoreCase(req.getSelectedAnswer());
        int pointsEarned = correct ? question.getPoints() : 0;

        MatchAnswer answer = MatchAnswer.builder()
                .match(match)
                .player(player)
                .question(question)
                .selectedAnswer(req.getSelectedAnswer())
                .correct(correct)
                .pointsEarned(pointsEarned)
                .timeTaken(req.getTimeTaken())
                .build();

        matchAnswerRepository.save(answer);

        // Update score
        boolean isPlayer1 = match.getPlayer1().getId().equals(userId);
        if (isPlayer1) {
            match.setPlayer1Score(match.getPlayer1Score() + pointsEarned);
        } else {
            match.setPlayer2Score(match.getPlayer2Score() + pointsEarned);
        }

        int updatedScore = isPlayer1 ? match.getPlayer1Score() : match.getPlayer2Score();
        int newAnsweredCount = answeredSoFar + 1;
        boolean nextAvailable = newAnsweredCount < TOTAL_QUESTIONS;

        // Check if both players finished
        int p1Answers = matchAnswerRepository.countByMatchIdAndPlayerId(matchId, match.getPlayer1().getId());
        int p2Answers = matchAnswerRepository.countByMatchIdAndPlayerId(matchId, match.getPlayer2().getId());

        if (p1Answers == TOTAL_QUESTIONS && p2Answers == TOTAL_QUESTIONS) {
            finishMatch(match);
        }

        matchRepository.save(match);
        matchEventPublisher.publishScoreUpdate(match); // ✅ after save

        return MatchAnswerResponse.builder()
                .correct(correct)
                .pointsEarned(pointsEarned)
                .updatedScore(updatedScore)
                .nextQuestionAvailable(nextAvailable)
                .correctAnswer(question.getCorrectAnswer())
                .build();
    }

    // ------------------------------------------------------------------ //
    //  RESULT
    // ------------------------------------------------------------------ //

    @Transactional(readOnly = true)
    public MatchResultResponse getResult(Long matchId, Long userId) {
        Match match = fetchMatch(matchId);
        assertPlayer(match, userId);

        String winnerUsername = null;
        if (match.getStatus() == MatchStatus.FINISHED) {
            if (match.getWinner() != null) {
                winnerUsername = match.getWinner().getUsername();
            } else {
                winnerUsername = "DRAW";
            }
        }

        return MatchResultResponse.builder()
                .matchId(match.getId())
                .status(match.getStatus())
                .player1Username(match.getPlayer1().getUsername())
                .player2Username(match.getPlayer2() != null ? match.getPlayer2().getUsername() : null)
                .player1Score(match.getPlayer1Score())
                .player2Score(match.getPlayer2Score())
                .winner(winnerUsername)
                .build();
    }

    // ------------------------------------------------------------------ //
    //  PRIVATE HELPERS
    // ------------------------------------------------------------------ //

    private void assignQuestions(Match match) {
        List<Question> questions = questionRepository.findRandomByQuizId(1L, TOTAL_QUESTIONS);
        if (questions.size() < TOTAL_QUESTIONS) {
            throw new BadRequestException(
                "Not enough questions in database. Need at least " + TOTAL_QUESTIONS + ".");
        }

        for (int i = 0; i < questions.size(); i++) {
            MatchQuestion mq = MatchQuestion.builder()
                    .match(match)
                    .question(questions.get(i))
                    .questionOrder(i)
                    .build();
            matchQuestionRepository.save(mq);
        }
    }

    private void finishMatch(Match match) {
        match.setStatus(MatchStatus.FINISHED);

        User p1 = match.getPlayer1();
        User p2 = match.getPlayer2();

        if (match.getPlayer1Score() > match.getPlayer2Score()) {
            match.setWinner(p1);
            eloService.updateRatings(p1, p2);       // ✅ ELO
        } else if (match.getPlayer2Score() > match.getPlayer1Score()) {
            match.setWinner(p2);
            eloService.updateRatings(p2, p1);       // ✅ ELO
        } else {
            // DRAW — winner stays null
            eloService.updateRatingsDraw(p1, p2);   // ✅ ELO
        }

        matchEventPublisher.publishMatchState(match, "MATCH_FINISHED"); // ✅ WebSocket
    }

    private Map<String, String> buildOptions(String optionsJson) {
        try {
            List<String> raw = objectMapper.readValue(optionsJson, new TypeReference<>() {});
            String[] labels = {"A", "B", "C", "D"};
            Map<String, String> options = new LinkedHashMap<>();
            for (int i = 0; i < raw.size() && i < labels.length; i++) {
                options.put(labels[i], raw.get(i));
            }
            return options;
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Malformed options JSON for question.");
        }
    }

    private Match fetchMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));
    }

    private User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    private void assertPlayer(Match match, Long userId) {
        boolean isPlayer1 = match.getPlayer1().getId().equals(userId);
        boolean isPlayer2 = match.getPlayer2() != null && match.getPlayer2().getId().equals(userId);
        if (!isPlayer1 && !isPlayer2) {
            throw new ForbiddenException("You are not a participant in this match.");
        }
    }

    private void assertActive(Match match) {
        if (match.getStatus() == MatchStatus.WAITING) {
            throw new BadRequestException("Match has not started yet.");
        }
        if (match.getStatus() == MatchStatus.FINISHED) {
            throw new BadRequestException("Match is already finished.");
        }
    }
}
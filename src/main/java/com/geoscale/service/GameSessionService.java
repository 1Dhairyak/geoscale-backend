package com.geoscale.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geoscale.dto.request.SubmitAnswerRequest;
import com.geoscale.dto.response.GameSessionResponse;
import com.geoscale.dto.response.QuestionResponse;
import com.geoscale.entity.*;
import com.geoscale.exception.BadRequestException;
import com.geoscale.exception.ResourceNotFoundException;
import com.geoscale.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class GameSessionService {
    private final GameSessionRepository sessionRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public GameSessionService(GameSessionRepository sessionRepository, QuizRepository quizRepository,
                              QuestionRepository questionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository; this.quizRepository = quizRepository;
        this.questionRepository = questionRepository; this.userRepository = userRepository;
    }
    @Transactional
    public GameSessionResponse startSession(Long quizId, String username) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz not found: " + quizId));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        List<Question> randomQuestions = questionRepository.findRandomByQuizId(quizId, 10);
        String ids = randomQuestions.stream().map(q -> q.getId().toString()).collect(Collectors.joining(","));
        GameSession session = GameSession.builder().user(user).quiz(quiz).totalQuestions(randomQuestions.size()).build();
        session.setSessionQuestionIds(ids);
        return toResponse(sessionRepository.save(session));
    }
    private List<Question> getSessionQuestions(GameSession session) {
        String ids = session.getSessionQuestionIds();
        if (ids != null && !ids.isBlank()) {
            List<Long> idList = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
            List<Question> questions = idList.stream()
                .map(id -> questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found: " + id)))
                .collect(Collectors.toList());
            for (int i = 0; i < questions.size(); i++) questions.get(i).setOrderIndex(i + 1);
            return questions;
        }
        return session.getQuiz().getQuestions();
    }
    @Transactional(readOnly = true)
    public QuestionResponse getQuestion(Long sessionId, int orderIndex, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        GameSession session = sessionRepository.findByIdAndUserId(sessionId, user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));
        List<Question> questions = getSessionQuestions(session);
        Question question = questions.stream().filter(q -> q.getOrderIndex() == orderIndex).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Question not found at index: " + orderIndex));
        List<String> options = new ArrayList<>();
        if (question.getOptionsJson() != null && !question.getOptionsJson().isBlank()) {
            try { options = objectMapper.readValue(question.getOptionsJson(), new TypeReference<List<String>>(){}); } catch (Exception e) {}
        }
        return new QuestionResponse(question.getId(), question.getPrompt(), question.getType().name(), options, question.getPoints(), question.getOrderIndex(), question.getCorrectAnswer());
    }
    @Transactional
    public GameSessionResponse submitAnswer(Long sessionId, SubmitAnswerRequest request, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        GameSession session = sessionRepository.findByIdAndUserId(sessionId, user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));
        if (session.getStatus() != GameSession.Status.IN_PROGRESS) throw new BadRequestException("Session is not in progress");
        List<Question> questions = getSessionQuestions(session);
        Question question = questions.stream().filter(q -> q.getId().equals(request.getQuestionId())).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Question not found in this session"));
        boolean correct = question.getCorrectAnswer().equalsIgnoreCase(request.getAnswer().trim());
        int awarded = correct ? question.getPoints() : 0;
        Answer answer = Answer.builder().session(session).question(question)
            .submittedAnswer(request.getAnswer()).correct(correct)
            .pointsAwarded(awarded).responseTimeMs(request.getResponseTimeMs()).build();
        session.getAnswers().add(answer);
        session.setScore(session.getScore() + awarded);
        session.setAnsweredQuestions(session.getAnsweredQuestions() + 1);
        if (session.getAnsweredQuestions() >= session.getTotalQuestions()) {
            session.setStatus(GameSession.Status.COMPLETED);
            session.setCompletedAt(Instant.now());
        }
        return toResponse(sessionRepository.save(session));
    }
    @Transactional
    public GameSessionResponse completeSession(Long sessionId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        GameSession session = sessionRepository.findByIdAndUserId(sessionId, user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));
        session.setStatus(GameSession.Status.COMPLETED); session.setCompletedAt(Instant.now());
        return toResponse(sessionRepository.save(session));
    }
    @Transactional(readOnly = true)
    public Page<GameSessionResponse> getUserSessions(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return sessionRepository.findAllByUserId(user.getId(), pageable).map(this::toResponse);
    }
    @Transactional(readOnly = true)
    public GameSessionResponse getSession(Long sessionId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return sessionRepository.findByIdAndUserId(sessionId, user.getId())
            .map(this::toResponse).orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));
    }
    private GameSessionResponse toResponse(GameSession s) {
        return GameSessionResponse.builder()
            .id(s.getId()).quizId(s.getQuiz().getId()).quizTitle(s.getQuiz().getTitle())
            .status(s.getStatus()).score(s.getScore())
            .totalQuestions(s.getTotalQuestions()).answeredQuestions(s.getAnsweredQuestions())
            .startedAt(s.getStartedAt()).completedAt(s.getCompletedAt()).build();
    }
}
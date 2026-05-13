package com.geoscale.controller;

import com.geoscale.repository.AnswerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionStatsController {

    private final AnswerRepository answerRepository;

    public QuestionStatsController(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @GetMapping("/{questionId}/stats")
    public ResponseEntity<?> getStats(@PathVariable Long questionId) {
        Map<String, Object> raw = answerRepository.findStatsByQuestion(questionId);

        if (raw == null || raw.get("total") == null || ((Number) raw.get("total")).longValue() == 0) {
            return ResponseEntity.ok(Map.of(
                "questionId",     questionId,
                "totalAttempts",  0,
                "correctAttempts", 0,
                "wrongAttempts",  0,
                "correctPercentage", 0.0,
                "wrongPercentage",   0.0,
                "averageResponseTimeMs", 0,
                "fastestMs",      0,
                "message",        "You're one of the first players to answer this! 👀"
            ));
        }

        long total   = ((Number) raw.get("total")).longValue();
        long correct = ((Number) raw.get("correct_count")).longValue();
        long wrong   = total - correct;
        double avg   = raw.get("avg_time_ms") != null ? ((Number) raw.get("avg_time_ms")).doubleValue() : 0.0;
        long fastest = raw.get("fastest_ms")  != null ? ((Number) raw.get("fastest_ms")).longValue()   : 0L;

        double correctPct = Math.round(correct * 100.0 / total * 10) / 10.0;
        double wrongPct   = Math.round(wrong   * 100.0 / total * 10) / 10.0;

        String message;
        if (total < 5) {
            message = "You're one of the first players to answer this! 👀";
        } else if (wrongPct > 70) {
            message = "This question destroys most players 💀";
        } else if (wrongPct > 50) {
            message = "Most players struggle with this one 😅";
        } else if (wrongPct > 30) {
            message = (int) wrongPct + "% of players got this wrong 😬";
        } else if (correctPct > 90) {
            message = "Almost everyone gets this right 😎";
        } else if (correctPct > 80) {
            message = "Players usually ace this one 🔥";
        } else if (correctPct > 60) {
            message = (int) correctPct + "% of players got this correct ✅";
        } else {
            message = "A tricky one — roughly half get it right 🤔";
        }

        return ResponseEntity.ok(Map.of(
            "questionId",            questionId,
            "totalAttempts",         total,
            "correctAttempts",       correct,
            "wrongAttempts",         wrong,
            "correctPercentage",     correctPct,
            "wrongPercentage",       wrongPct,
            "averageResponseTimeMs", Math.round(avg),
            "fastestMs",             fastest,
            "message",               message
        ));
    }
}
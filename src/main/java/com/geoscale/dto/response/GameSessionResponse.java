package com.geoscale.dto.response;

import com.geoscale.entity.GameSession;
import java.time.Instant;

public class GameSessionResponse {
    @com.fasterxml.jackson.annotation.JsonProperty private Long id;
    @com.fasterxml.jackson.annotation.JsonProperty private Long quizId;
    @com.fasterxml.jackson.annotation.JsonProperty private String quizTitle;
    @com.fasterxml.jackson.annotation.JsonProperty private GameSession.Status status;
    @com.fasterxml.jackson.annotation.JsonProperty private int score;
    @com.fasterxml.jackson.annotation.JsonProperty private int totalQuestions;
    @com.fasterxml.jackson.annotation.JsonProperty private int answeredQuestions;
    @com.fasterxml.jackson.annotation.JsonProperty private Instant startedAt;
    @com.fasterxml.jackson.annotation.JsonProperty private Instant completedAt;

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id, quizId; private String quizTitle;
        private GameSession.Status status; private int score, totalQuestions, answeredQuestions;
        private Instant startedAt, completedAt;
        public Builder id(Long v) { this.id = v; return this; }
        public Builder quizId(Long v) { this.quizId = v; return this; }
        public Builder quizTitle(String v) { this.quizTitle = v; return this; }
        public Builder status(GameSession.Status v) { this.status = v; return this; }
        public Builder score(int v) { this.score = v; return this; }
        public Builder totalQuestions(int v) { this.totalQuestions = v; return this; }
        public Builder answeredQuestions(int v) { this.answeredQuestions = v; return this; }
        public Builder startedAt(Instant v) { this.startedAt = v; return this; }
        public Builder completedAt(Instant v) { this.completedAt = v; return this; }
        public GameSessionResponse build() {
            GameSessionResponse r = new GameSessionResponse();
            r.id = id; r.quizId = quizId; r.quizTitle = quizTitle; r.status = status;
            r.score = score; r.totalQuestions = totalQuestions;
            r.answeredQuestions = answeredQuestions;
            r.startedAt = startedAt; r.completedAt = completedAt;
            return r;
        }
    }

    public Long getId() { return id; }
    public Long getQuizId() { return quizId; }
    public String getQuizTitle() { return quizTitle; }
    public GameSession.Status getStatus() { return status; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getAnsweredQuestions() { return answeredQuestions; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
}

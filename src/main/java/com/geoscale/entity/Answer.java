package com.geoscale.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "answers")
@EntityListeners(AuditingEntityListener.class)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private GameSession session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String submittedAnswer;

    @Column(nullable = false)
    private boolean correct;

    @Column(nullable = false)
    private int pointsAwarded = 0;

    private Long responseTimeMs;

    @CreatedDate
    @Column(updatable = false)
    private Instant answeredAt;

    public Answer() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private GameSession session; private Question question;
        private String submittedAnswer; private boolean correct;
        private int pointsAwarded = 0; private Long responseTimeMs;
        public Builder session(GameSession v) { this.session = v; return this; }
        public Builder question(Question v) { this.question = v; return this; }
        public Builder submittedAnswer(String v) { this.submittedAnswer = v; return this; }
        public Builder correct(boolean v) { this.correct = v; return this; }
        public Builder pointsAwarded(int v) { this.pointsAwarded = v; return this; }
        public Builder responseTimeMs(Long v) { this.responseTimeMs = v; return this; }
        public Answer build() {
            Answer a = new Answer();
            a.session = this.session; a.question = this.question;
            a.submittedAnswer = this.submittedAnswer; a.correct = this.correct;
            a.pointsAwarded = this.pointsAwarded; a.responseTimeMs = this.responseTimeMs;
            return a;
        }
    }

    public Long getId() { return id; }
    public GameSession getSession() { return session; }
    public Question getQuestion() { return question; }
    public String getSubmittedAnswer() { return submittedAnswer; }
    public boolean isCorrect() { return correct; }
    public int getPointsAwarded() { return pointsAwarded; }
    public Long getResponseTimeMs() { return responseTimeMs; }
    public Instant getAnsweredAt() { return answeredAt; }
}

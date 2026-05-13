package com.geoscale.entity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "game_sessions")
@EntityListeners(AuditingEntityListener.class)
public class GameSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "quiz_id", nullable = false) private Quiz quiz;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status = Status.IN_PROGRESS;
    @Column(nullable = false) private int score = 0;
    @Column(nullable = false) private int totalQuestions = 0;
    @Column(nullable = false) private int answeredQuestions = 0;
    @CreatedDate @Column(updatable = false) private Instant startedAt;
    private Instant completedAt;
    @Column(name = "session_question_ids") private String sessionQuestionIds;
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY) private List<Answer> answers = new ArrayList<>();
    public enum Status { IN_PROGRESS, COMPLETED, ABANDONED }
    public GameSession() {}
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private User user; private Quiz quiz; private int totalQuestions = 0;
        public Builder user(User v) { this.user = v; return this; }
        public Builder quiz(Quiz v) { this.quiz = v; return this; }
        public Builder totalQuestions(int v) { this.totalQuestions = v; return this; }
        public GameSession build() {
            GameSession s = new GameSession();
            s.user = this.user; s.quiz = this.quiz; s.totalQuestions = this.totalQuestions; s.answers = new ArrayList<>();
            return s;
        }
    }
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Quiz getQuiz() { return quiz; }
    public Status getStatus() { return status; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getAnsweredQuestions() { return answeredQuestions; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public List<Answer> getAnswers() { return answers; }
    public String getSessionQuestionIds() { return sessionQuestionIds; }
    public void setStatus(Status v) { this.status = v; }
    public void setScore(int v) { this.score = v; }
    public void setTotalQuestions(int v) { this.totalQuestions = v; }
    public void setAnsweredQuestions(int v) { this.answeredQuestions = v; }
    public void setCompletedAt(Instant v) { this.completedAt = v; }
    public void setAnswers(List<Answer> v) { this.answers = v; }
    public void setSessionQuestionIds(String v) { this.sessionQuestionIds = v; }
}
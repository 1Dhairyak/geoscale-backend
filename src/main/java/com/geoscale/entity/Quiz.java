package com.geoscale.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty = Difficulty.MEDIUM;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    public enum Difficulty { EASY, MEDIUM, HARD, EXPERT }

    public Quiz() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String title, description;
        private Difficulty difficulty = Difficulty.MEDIUM;
        private boolean active = true;
        public Builder title(String v) { this.title = v; return this; }
        public Builder description(String v) { this.description = v; return this; }
        public Builder difficulty(Difficulty v) { this.difficulty = v; return this; }
        public Builder active(boolean v) { this.active = v; return this; }
        public Quiz build() {
            Quiz q = new Quiz();
            q.title = this.title; q.description = this.description;
            q.difficulty = this.difficulty; q.active = this.active;
            return q;
        }
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Difficulty getDifficulty() { return difficulty; }
    public boolean isActive() { return active; }
    public List<Question> getQuestions() { return questions; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setActive(boolean active) { this.active = active; }
}

package com.geoscale.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false, length = 1000)
    private String prompt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Column(columnDefinition = "TEXT")
    private String optionsJson;

    @Column(nullable = false)
    private String correctAnswer;

    @Column(nullable = false)
    private int points = 10;

    @Column(nullable = false)
    private int orderIndex = 0;

    public enum QuestionType { MULTIPLE_CHOICE, TRUE_FALSE, TEXT_INPUT, MAP_CLICK }

    public Question() {}

    public Long getId() { return id; }
    public Quiz getQuiz() { return quiz; }
    public String getPrompt() { return prompt; }
    public QuestionType getType() { return type; }
    public String getOptionsJson() { return optionsJson; }
    public String getCorrectAnswer() { return correctAnswer; }
    public int getPoints() { return points; }
    public int getOrderIndex() { return orderIndex; }

    public void setId(Long id) { this.id = id; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public void setPoints(int points) { this.points = points; }

    public void setOrderIndex(int v) { this.orderIndex = v; }
}

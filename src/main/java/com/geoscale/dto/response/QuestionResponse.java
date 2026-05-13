package com.geoscale.dto.response;
import java.util.List;
public class QuestionResponse {
    private Long id;
    private String prompt;
    private String type;
    private List<String> options;
    private int points;
    private int orderIndex;
    private String correctAnswer;
    public QuestionResponse(Long id, String prompt, String type, List<String> options, int points, int orderIndex, String correctAnswer) {
        this.id=id; this.prompt=prompt; this.type=type; this.options=options;
        this.points=points; this.orderIndex=orderIndex; this.correctAnswer=correctAnswer;
    }
    public Long getId() { return id; }
    public String getPrompt() { return prompt; }
    public String getType() { return type; }
    public List<String> getOptions() { return options; }
    public int getPoints() { return points; }
    public int getOrderIndex() { return orderIndex; }
    public String getCorrectAnswer() { return correctAnswer; }
}

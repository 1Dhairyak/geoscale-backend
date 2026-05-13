package com.geoscale.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubmitAnswerRequest {
    @NotNull private Long questionId;
    @NotBlank private String answer;
    private Long responseTimeMs;

    public Long getQuestionId() { return questionId; }
    public String getAnswer() { return answer; }
    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public void setAnswer(String answer) { this.answer = answer; }
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
}

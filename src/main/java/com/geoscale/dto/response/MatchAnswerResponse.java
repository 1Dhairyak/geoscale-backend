package com.geoscale.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchAnswerResponse {
    private boolean correct;
    private int pointsEarned;
    private int updatedScore;
    private boolean nextQuestionAvailable;
    private String correctAnswer; // revealed after submission
}

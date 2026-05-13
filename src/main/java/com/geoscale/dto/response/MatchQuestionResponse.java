package com.geoscale.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class MatchQuestionResponse {
    private int questionIndex;      // 1-based
    private Long questionId;
    private String prompt;
    private Map<String, String> options; // {"A": "Paris", "B": "London", ...}
    private int points;
    private int totalQuestions;
}

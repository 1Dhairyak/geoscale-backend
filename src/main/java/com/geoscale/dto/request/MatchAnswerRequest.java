package com.geoscale.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchAnswerRequest {

    @NotNull(message = "questionId is required")
    private Long questionId;

    @NotBlank(message = "selectedAnswer is required")
    @Pattern(regexp = "[ABCD]", message = "selectedAnswer must be A, B, C, or D")
    private String selectedAnswer;

    @NotNull(message = "timeTaken is required")
    private Integer timeTaken; // milliseconds
}

package com.geoscale.dto.response;

import com.geoscale.entity.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchResultResponse {
    private Long matchId;
    private MatchStatus status;
    private String player1Username;
    private String player2Username;
    private int player1Score;
    private int player2Score;
    private String winner;  // username or "DRAW" or null if still in progress
}

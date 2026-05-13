package com.geoscale.dto.ws;

import com.geoscale.entity.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchStateMessage {
    private Long matchId;
    private MatchStatus status;
    private String event;
    private String winner;
    private int player1Score;
    private int player2Score;
}

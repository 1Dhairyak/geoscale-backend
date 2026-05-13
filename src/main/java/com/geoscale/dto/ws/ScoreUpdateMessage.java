package com.geoscale.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreUpdateMessage {
    private Long matchId;
    private String player1Username;
    private String player2Username;
    private int player1Score;
    private int player2Score;
}

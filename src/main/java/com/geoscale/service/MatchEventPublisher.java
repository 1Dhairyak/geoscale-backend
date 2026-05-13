package com.geoscale.service;

import com.geoscale.dto.ws.MatchStateMessage;
import com.geoscale.dto.ws.ScoreUpdateMessage;
import com.geoscale.entity.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishScoreUpdate(Match match) {
        ScoreUpdateMessage msg = ScoreUpdateMessage.builder()
                .matchId(match.getId())
                .player1Username(match.getPlayer1().getUsername())
                .player2Username(match.getPlayer2().getUsername())
                .player1Score(match.getPlayer1Score())
                .player2Score(match.getPlayer2Score())
                .build();

        messagingTemplate.convertAndSend(
            "/topic/match/" + match.getId() + "/score", msg);
    }

    public void publishMatchState(Match match, String event) {
        String winner = null;
        if (match.getWinner() != null) {
            winner = match.getWinner().getUsername();
        } else if ("MATCH_FINISHED".equals(event)) {
            winner = "DRAW";
        }

        MatchStateMessage msg = MatchStateMessage.builder()
                .matchId(match.getId())
                .status(match.getStatus())
                .event(event)
                .winner(winner)
                .player1Score(match.getPlayer1Score())
                .player2Score(match.getPlayer2Score())
                .build();

        messagingTemplate.convertAndSend(
            "/topic/match/" + match.getId() + "/state", msg);
    }
}

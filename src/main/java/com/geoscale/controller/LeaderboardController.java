package com.geoscale.controller;

import com.geoscale.dto.response.AccuracyLeaderboardEntry;
import com.geoscale.dto.response.GlobalLeaderboardEntry;
import com.geoscale.dto.response.SpeedLeaderboardEntry;
import com.geoscale.service.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/global")
    public ResponseEntity<List<GlobalLeaderboardEntry>> getGlobalLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getGlobalLeaderboard());
    }

    @GetMapping("/accuracy")
    public ResponseEntity<List<AccuracyLeaderboardEntry>> getAccuracyLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getAccuracyLeaderboard());
    }

    @GetMapping("/speed")
    public ResponseEntity<List<SpeedLeaderboardEntry>> getSpeedLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getSpeedLeaderboard());
    }
}
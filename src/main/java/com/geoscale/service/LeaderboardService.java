package com.geoscale.service;

import com.geoscale.dto.response.AccuracyLeaderboardEntry;
import com.geoscale.dto.response.GlobalLeaderboardEntry;
import com.geoscale.dto.response.SpeedLeaderboardEntry;
import com.geoscale.repository.LeaderboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;

    public LeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    public List<GlobalLeaderboardEntry> getGlobalLeaderboard() {
        List<Object[]> rows = leaderboardRepository.findGlobalLeaderboard();
        List<GlobalLeaderboardEntry> result = new ArrayList<>();
        int rank = 1;
        for (Object[] row : rows) {
            String username = (String) row[0];
            long totalScore = row[1] instanceof Number ? ((Number) row[1]).longValue() : 0L;
            long gamesPlayed = row[2] instanceof Number ? ((Number) row[2]).longValue() : 0L;
            result.add(new GlobalLeaderboardEntry(rank++, username, totalScore, gamesPlayed));
        }
        return result;
    }

    public List<AccuracyLeaderboardEntry> getAccuracyLeaderboard() {
        try {
            List<Object[]> rows = leaderboardRepository.findAccuracyLeaderboard();
            List<AccuracyLeaderboardEntry> result = new ArrayList<>();
            int rank = 1;
            for (Object[] row : rows) {
                String username = (String) row[0];
                double accuracy = row[1] instanceof Number ? ((Number) row[1]).doubleValue() : 0.0;
                long attempts = row[2] instanceof Number ? ((Number) row[2]).longValue() : 0L;
                result.add(new AccuracyLeaderboardEntry(rank++, username,
                        Math.round(accuracy * 10.0) / 10.0, attempts));
            }
            return result;
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<SpeedLeaderboardEntry> getSpeedLeaderboard() {
        try {
            List<Object[]> rows = leaderboardRepository.findSpeedLeaderboard();
            List<SpeedLeaderboardEntry> result = new ArrayList<>();
            int rank = 1;
            for (Object[] row : rows) {
                String username = (String) row[0];
                double avgTime = row[1] instanceof Number ? ((Number) row[1]).doubleValue() : 0.0;
                long correct = row[2] instanceof Number ? ((Number) row[2]).longValue() : 0L;
                result.add(new SpeedLeaderboardEntry(rank++, username,
                        Math.round(avgTime * 10.0) / 10.0, correct));
            }
            return result;
        } catch (Exception e) {
            return List.of();
        }
    }
}
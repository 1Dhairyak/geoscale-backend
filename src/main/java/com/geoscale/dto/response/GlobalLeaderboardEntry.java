package com.geoscale.dto.response;

public class GlobalLeaderboardEntry {
    private int rank;
    private String username;
    private long totalScore;
    private long gamesPlayed;

    public GlobalLeaderboardEntry(int rank, String username, long totalScore, long gamesPlayed) {
        this.rank = rank;
        this.username = username;
        this.totalScore = totalScore;
        this.gamesPlayed = gamesPlayed;
    }

    public int getRank() { return rank; }
    public String getUsername() { return username; }
    public long getTotalScore() { return totalScore; }
    public long getGamesPlayed() { return gamesPlayed; }
}
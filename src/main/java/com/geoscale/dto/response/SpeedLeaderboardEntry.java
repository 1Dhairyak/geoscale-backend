package com.geoscale.dto.response;

public class SpeedLeaderboardEntry {
    private int rank;
    private String username;
    private double averageResponseTime;
    private long correctAnswers;

    public SpeedLeaderboardEntry(int rank, String username, double averageResponseTime, long correctAnswers) {
        this.rank = rank;
        this.username = username;
        this.averageResponseTime = averageResponseTime;
        this.correctAnswers = correctAnswers;
    }

    public int getRank() { return rank; }
    public String getUsername() { return username; }
    public double getAverageResponseTime() { return averageResponseTime; }
    public long getCorrectAnswers() { return correctAnswers; }
}
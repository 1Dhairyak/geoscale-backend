package com.geoscale.dto.response;

public class AccuracyLeaderboardEntry {
    private int rank;
    private String username;
    private double accuracyPercentage;
    private long totalAttempts;

    public AccuracyLeaderboardEntry(int rank, String username, double accuracyPercentage, long totalAttempts) {
        this.rank = rank;
        this.username = username;
        this.accuracyPercentage = accuracyPercentage;
        this.totalAttempts = totalAttempts;
    }

    public int getRank() { return rank; }
    public String getUsername() { return username; }
    public double getAccuracyPercentage() { return accuracyPercentage; }
    public long getTotalAttempts() { return totalAttempts; }
}
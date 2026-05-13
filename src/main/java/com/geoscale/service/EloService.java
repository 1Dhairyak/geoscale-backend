package com.geoscale.service;

import com.geoscale.entity.User;
import com.geoscale.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EloService {

    private static final int K = 32;
    private final UserRepository userRepository;

    public void updateRatings(User winner, User loser) {
        int rW = winner.getEloRating();
        int rL = loser.getEloRating();
        double expectedW = 1.0 / (1 + Math.pow(10, (rL - rW) / 400.0));
        double expectedL = 1.0 - expectedW;
        winner.setEloRating((int) Math.round(rW + K * (1 - expectedW)));
        loser.setEloRating((int) Math.round(rL + K * (0 - expectedL)));
        userRepository.save(winner);
        userRepository.save(loser);
    }

    public void updateRatingsDraw(User p1, User p2) {
        int r1 = p1.getEloRating();
        int r2 = p2.getEloRating();
        double expected1 = 1.0 / (1 + Math.pow(10, (r2 - r1) / 400.0));
        double expected2 = 1.0 - expected1;
        p1.setEloRating((int) Math.round(r1 + K * (0.5 - expected1)));
        p2.setEloRating((int) Math.round(r2 + K * (0.5 - expected2)));
        userRepository.save(p1);
        userRepository.save(p2);
    }
}
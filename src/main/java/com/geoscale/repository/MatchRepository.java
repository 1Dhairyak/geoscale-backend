package com.geoscale.repository;

import com.geoscale.entity.Match;
import com.geoscale.entity.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // All WAITING matches (lobby browser, optional future use)
    List<Match> findByStatus(MatchStatus status);

    // All matches a user is part of
    @Query("SELECT m FROM Match m WHERE m.player1.id = :userId OR m.player2.id = :userId")
    List<Match> findAllByPlayer(@Param("userId") Long userId);

    // Active match for a user (should be at most 1)
    @Query("SELECT m FROM Match m WHERE (m.player1.id = :userId OR m.player2.id = :userId) AND m.status = 'ACTIVE'")
    java.util.Optional<Match> findActiveMatchForPlayer(@Param("userId") Long userId);
}

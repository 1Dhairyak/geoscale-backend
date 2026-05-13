package com.geoscale.repository;

import com.geoscale.entity.MatchAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchAnswerRepository extends JpaRepository<MatchAnswer, Long> {

    @Query("SELECT ma FROM MatchAnswer ma WHERE ma.match.id = :matchId AND ma.player.id = :playerId ORDER BY ma.id ASC")
    List<MatchAnswer> findByMatchIdAndPlayerIdOrderByQuestionOrder(
        @Param("matchId") Long matchId,
        @Param("playerId") Long playerId
    );

    int countByMatchIdAndPlayerId(Long matchId, Long playerId);

    boolean existsByMatchIdAndPlayerIdAndQuestionId(Long matchId, Long playerId, Long questionId);

    @Query("SELECT ma FROM MatchAnswer ma WHERE ma.match.id = :matchId AND ma.player.id = :playerId AND ma.question.id = :questionId")
    Optional<MatchAnswer> findByMatchIdAndPlayerIdAndMatchQuestionId(
        @Param("matchId") Long matchId,
        @Param("playerId") Long playerId,
        @Param("questionId") Long questionId
    );

    List<MatchAnswer> findByMatchId(Long matchId);
}
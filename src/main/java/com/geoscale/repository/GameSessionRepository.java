package com.geoscale.repository;

import com.geoscale.entity.GameSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

    Page<GameSession> findAllByUserId(Long userId, Pageable pageable);

    Optional<GameSession> findByIdAndUserId(Long sessionId, Long userId);

    List<GameSession> findAllByUserIdAndStatus(Long userId, GameSession.Status status);

    @Query("""
            SELECT gs FROM GameSession gs
            WHERE gs.user.id = :userId
            ORDER BY gs.score DESC
            LIMIT :limit
            """)
    List<GameSession> findTopSessionsByUser(Long userId, int limit);
}

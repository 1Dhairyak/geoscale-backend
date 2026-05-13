package com.geoscale.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.geoscale.entity.User;
import java.util.List;

@Repository
public interface LeaderboardRepository extends JpaRepository<User, Long> {

    @Query(value = """
        SELECT u.username, COALESCE(SUM(gs.score), 0) AS total_score, COUNT(gs.id) AS games_played
        FROM users u
        LEFT JOIN game_sessions gs ON gs.user_id = u.id AND gs.status = 'COMPLETED'
        GROUP BY u.id, u.username
        ORDER BY total_score DESC, games_played DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findGlobalLeaderboard();

    @Query(value = """
        SELECT u.username,
               COUNT(CASE WHEN a.correct = true THEN 1 END) * 100.0 / COUNT(a.id) AS accuracy_pct,
               COUNT(a.id) AS total_attempts
        FROM users u
        JOIN game_sessions gs ON gs.user_id = u.id
        JOIN answers a ON a.session_id = gs.id
        GROUP BY u.id, u.username
        HAVING COUNT(a.id) >= 5
        ORDER BY accuracy_pct DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findAccuracyLeaderboard();

    @Query(value = """
        SELECT u.username,
               AVG(a.response_time_ms) AS avg_response_time,
               COUNT(a.id) AS correct_answers
        FROM users u
        JOIN game_sessions gs ON gs.user_id = u.id
        JOIN answers a ON a.session_id = gs.id AND a.correct = true
        GROUP BY u.id, u.username
        HAVING COUNT(a.id) >= 3
        ORDER BY avg_response_time ASC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findSpeedLeaderboard();
}
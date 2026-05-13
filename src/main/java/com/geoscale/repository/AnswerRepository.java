package com.geoscale.repository;

import com.geoscale.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(value = """
            SELECT
                COUNT(a.id)                                                        AS total,
                SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END)                 AS correct_count,
                AVG(a.response_time_ms)                                            AS avg_time_ms,
                MIN(a.response_time_ms)                                            AS fastest_ms
            FROM answers a
            WHERE a.question_id = :questionId
            """, nativeQuery = true)
    Map<String, Object> findStatsByQuestion(@Param("questionId") Long questionId);
}
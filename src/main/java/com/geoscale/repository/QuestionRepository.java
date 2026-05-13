package com.geoscale.repository;
import com.geoscale.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM questions WHERE quiz_id = :quizId ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomByQuizId(@Param("quizId") Long quizId, @Param("limit") int limit);
}
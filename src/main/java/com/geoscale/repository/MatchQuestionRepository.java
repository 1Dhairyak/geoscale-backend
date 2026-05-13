package com.geoscale.repository;

import com.geoscale.entity.MatchQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchQuestionRepository extends JpaRepository<MatchQuestion, Long> {

    List<MatchQuestion> findByMatchIdOrderByQuestionOrder(Long matchId);

    Optional<MatchQuestion> findByMatchIdAndQuestionOrder(Long matchId, int questionOrder);

    int countByMatchId(Long matchId);
}

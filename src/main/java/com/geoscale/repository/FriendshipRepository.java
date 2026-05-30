package com.geoscale.repository;

import com.geoscale.entity.Friendship;
import com.geoscale.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship f WHERE f.status = 'ACCEPTED' AND (f.requester = :user OR f.addressee = :user)")
    List<Friendship> findAcceptedFriendships(@Param("user") User user);

    @Query("SELECT f FROM Friendship f WHERE f.addressee = :user AND f.status = 'PENDING'")
    List<Friendship> findPendingRequests(@Param("user") User user);

    @Query("SELECT f FROM Friendship f WHERE f.requester = :user AND f.status = 'PENDING'")
    List<Friendship> findSentRequests(@Param("user") User user);

    @Query("SELECT f FROM Friendship f WHERE (f.requester = :a AND f.addressee = :b) OR (f.requester = :b AND f.addressee = :a)")
    Optional<Friendship> findBetween(@Param("a") User a, @Param("b") User b);
}
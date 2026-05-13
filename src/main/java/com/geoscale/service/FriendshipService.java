package com.geoscale.service;

import com.geoscale.entity.Friendship;
import com.geoscale.entity.Match;
import com.geoscale.entity.User;
import com.geoscale.entity.enums.MatchStatus;
import com.geoscale.repository.FriendshipRepository;
import com.geoscale.repository.MatchRepository;
import com.geoscale.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    public FriendshipService(FriendshipRepository friendshipRepository,
                             UserRepository userRepository,
                             MatchRepository matchRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username));
    }

    @Transactional
    public Map<String, Object> sendRequest(String requesterUsername, String addresseeUsername) {
        if (requesterUsername.equals(addresseeUsername))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add yourself");

        User requester = getUser(requesterUsername);
        User addressee = getUser(addresseeUsername);

        friendshipRepository.findBetween(requester, addressee).ifPresent(f -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Friendship already exists with status: " + f.getStatus());
        });

        friendshipRepository.save(new Friendship(requester, addressee));
        return Map.of("message", "Friend request sent to " + addresseeUsername);
    }

    @Transactional
    public Map<String, Object> respondToRequest(String addresseeUsername, Long friendshipId, boolean accept) {
        Friendship f = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        if (!f.getAddressee().getUsername().equals(addresseeUsername))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your request");

        if (f.getStatus() != Friendship.Status.PENDING)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request already handled");

        f.setStatus(accept ? Friendship.Status.ACCEPTED : Friendship.Status.REJECTED);
        friendshipRepository.save(f);
        return Map.of("message", accept ? "Friend request accepted" : "Friend request rejected");
    }

    @Transactional
    public Map<String, Object> removeFriend(String username, String friendUsername) {
        User user   = getUser(username);
        User friend = getUser(friendUsername);
        Friendship f = friendshipRepository.findBetween(user, friend)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friendship not found"));
        friendshipRepository.delete(f);
        return Map.of("message", friendUsername + " removed from friends");
    }

    public List<Map<String, Object>> getFriends(String username) {
        User user = getUser(username);
        return friendshipRepository.findAcceptedFriendships(user).stream().map(f -> {
            User friend = f.getRequester().getUsername().equals(username)
                    ? f.getAddressee() : f.getRequester();
            return Map.<String, Object>of(
                    "friendshipId", f.getId(),
                    "username", friend.getUsername(),
                    "eloRating", friend.getEloRating(),
                    "since", f.getCreatedAt().toString()
            );
        }).toList();
    }

    public List<Map<String, Object>> getPendingRequests(String username) {
        User user = getUser(username);
        return friendshipRepository.findPendingRequests(user).stream().map(f ->
                Map.<String, Object>of(
                        "friendshipId", f.getId(),
                        "from", f.getRequester().getUsername(),
                        "eloRating", f.getRequester().getEloRating(),
                        "since", f.getCreatedAt().toString()
                )).toList();
    }

    public List<Map<String, Object>> getSentRequests(String username) {
        User user = getUser(username);
        return friendshipRepository.findSentRequests(user).stream().map(f ->
                Map.<String, Object>of(
                        "friendshipId", f.getId(),
                        "to", f.getAddressee().getUsername(),
                        "eloRating", f.getAddressee().getEloRating(),
                        "since", f.getCreatedAt().toString()
                )).toList();
    }

    @Transactional
    public Map<String, Object> challengeFriend(String challengerUsername, String friendUsername) {
        User challenger = getUser(challengerUsername);
        User friend     = getUser(friendUsername);

        friendshipRepository.findBetween(challenger, friend)
                .filter(f -> f.getStatus() == Friendship.Status.ACCEPTED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not friends with " + friendUsername));

        Match match = Match.builder()
                .player1(challenger)
                .player2(friend)
                .status(MatchStatus.WAITING)
                .build();
        matchRepository.save(match);

        return Map.of("matchId", match.getId(), "message", "Challenge sent to " + friendUsername);
    }
}
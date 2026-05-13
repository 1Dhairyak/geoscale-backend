package com.geoscale.controller;

import com.geoscale.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping
    public ResponseEntity<?> getFriends(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(friendshipService.getFriends(user.getUsername()));
    }

    @GetMapping("/requests/pending")
    public ResponseEntity<?> getPending(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(friendshipService.getPendingRequests(user.getUsername()));
    }

    @GetMapping("/requests/sent")
    public ResponseEntity<?> getSent(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(friendshipService.getSentRequests(user.getUsername()));
    }

    @PostMapping("/request/{username}")
    public ResponseEntity<?> sendRequest(@AuthenticationPrincipal UserDetails user,
                                         @PathVariable String username) {
        return ResponseEntity.ok(friendshipService.sendRequest(user.getUsername(), username));
    }

    @PostMapping("/request/{friendshipId}/accept")
    public ResponseEntity<?> accept(@AuthenticationPrincipal UserDetails user,
                                    @PathVariable Long friendshipId) {
        return ResponseEntity.ok(friendshipService.respondToRequest(user.getUsername(), friendshipId, true));
    }

    @PostMapping("/request/{friendshipId}/reject")
    public ResponseEntity<?> reject(@AuthenticationPrincipal UserDetails user,
                                    @PathVariable Long friendshipId) {
        return ResponseEntity.ok(friendshipService.respondToRequest(user.getUsername(), friendshipId, false));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> removeFriend(@AuthenticationPrincipal UserDetails user,
                                          @PathVariable String username) {
        return ResponseEntity.ok(friendshipService.removeFriend(user.getUsername(), username));
    }

    @PostMapping("/challenge/{username}")
    public ResponseEntity<?> challenge(@AuthenticationPrincipal UserDetails user,
                                       @PathVariable String username) {
        return ResponseEntity.ok(friendshipService.challengeFriend(user.getUsername(), username));
    }
}
package com.geoscale.controller;

import com.geoscale.dto.request.MatchAnswerRequest;
import com.geoscale.dto.response.*;
import com.geoscale.repository.UserRepository;
import com.geoscale.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final UserRepository userRepository;

    /**
     * POST /api/v1/match/create
     * Authenticated player creates a new match (status: WAITING).
     */
    @PostMapping("/create")
    public ResponseEntity<MatchCreateResponse> createMatch(
            @AuthenticationPrincipal UserDetails principal) {

        Long userId = resolveUserId(principal);
        MatchCreateResponse response = matchService.createMatch(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/v1/match/join/{matchId}
     * Second player joins an open match (status → ACTIVE, 10 Qs assigned).
     */
    @PostMapping("/join/{matchId}")
    public ResponseEntity<MatchCreateResponse> joinMatch(
            @PathVariable Long matchId,
            @AuthenticationPrincipal UserDetails principal) {

        Long userId = resolveUserId(principal);
        MatchCreateResponse response = matchService.joinMatch(matchId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/match/{matchId}/question
     * Get the next unanswered question for the calling player.
     */
    @GetMapping("/{matchId}/question")
    public ResponseEntity<MatchQuestionResponse> getNextQuestion(
            @PathVariable Long matchId,
            @AuthenticationPrincipal UserDetails principal) {

        Long userId = resolveUserId(principal);
        MatchQuestionResponse response = matchService.getNextQuestion(matchId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/match/{matchId}/answer
     * Submit an answer for the current question.
     */
    @PostMapping("/{matchId}/answer")
    public ResponseEntity<MatchAnswerResponse> submitAnswer(
            @PathVariable Long matchId,
            @Valid @RequestBody MatchAnswerRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        Long userId = resolveUserId(principal);
        MatchAnswerResponse response = matchService.submitAnswer(matchId, userId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/match/{matchId}/result
     * Get current scores / final result.
     */
    @GetMapping("/{matchId}/result")
    public ResponseEntity<MatchResultResponse> getResult(
            @PathVariable Long matchId,
            @AuthenticationPrincipal UserDetails principal) {

        Long userId = resolveUserId(principal);
        MatchResultResponse response = matchService.getResult(matchId, userId);
        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------------ //

    /**
     * Resolves the authenticated user's DB id from their JWT-injected UserDetails.
     * Never trusts any id from the request body/params.
     */
    private Long resolveUserId(UserDetails principal) {
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB"))
                .getId();
    }
}

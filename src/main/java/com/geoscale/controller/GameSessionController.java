package com.geoscale.controller;
import com.geoscale.dto.request.SubmitAnswerRequest;
import com.geoscale.dto.response.GameSessionResponse;
import com.geoscale.dto.response.QuestionResponse;
import com.geoscale.service.GameSessionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
public class GameSessionController {
    private final GameSessionService sessionService;
    public GameSessionController(GameSessionService sessionService) { this.sessionService = sessionService; }

    @PostMapping("/start/{quizId}")
    public ResponseEntity<GameSessionResponse> startSession(@PathVariable Long quizId, @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.startSession(quizId, principal.getUsername()));
    }

    @GetMapping("/{sessionId}/question/{orderIndex}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long sessionId, @PathVariable int orderIndex, @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(sessionService.getQuestion(sessionId, orderIndex, principal.getUsername()));
    }

    @PostMapping("/{sessionId}/answer")
    public ResponseEntity<GameSessionResponse> submitAnswer(@PathVariable Long sessionId, @Valid @RequestBody SubmitAnswerRequest request, @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(sessionService.submitAnswer(sessionId, request, principal.getUsername()));
    }

    @PatchMapping("/{sessionId}/complete")
    public ResponseEntity<GameSessionResponse> completeSession(@PathVariable Long sessionId, @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(sessionService.completeSession(sessionId, principal.getUsername()));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<GameSessionResponse> getSession(@PathVariable Long sessionId, @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(sessionService.getSession(sessionId, principal.getUsername()));
    }

    @GetMapping
    public ResponseEntity<Page<GameSessionResponse>> getUserSessions(@AuthenticationPrincipal UserDetails principal, @PageableDefault(size = 10, sort = "startedAt") Pageable pageable) {
        return ResponseEntity.ok(sessionService.getUserSessions(principal.getUsername(), pageable));
    }
}
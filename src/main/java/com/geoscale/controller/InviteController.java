package com.geoscale.controller;

import com.geoscale.dto.request.InviteRequest;
import com.geoscale.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InviteController {

    private final EmailService emailService;

    @PostMapping("/invite")
    public ResponseEntity<?> sendInvite(@RequestBody InviteRequest request) {
        emailService.sendInvite(request.getEmail(), request.getMessage());
        return ResponseEntity.ok(Map.of(
            "message", "Invite sent successfully to " + request.getEmail()
        ));
    }
}
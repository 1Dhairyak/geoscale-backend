package com.geoscale.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendInvite(String toEmail, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("You've been invited to GeoScale!");
        mail.setText(
            "Hey!\n\n" +
            message + "\n\n" +
            "Join GeoScale — the geography quiz battle game!\n" +
            "Sign up at: http://localhost:8080\n\n" +
            "See you on the leaderboard!"
        );
        mailSender.send(mail);
    }
}
package com.rekrutmen.rest_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otpCode);
        message.setFrom("seneng.banged@gmail.com");

        mailSender.send(message);
    }

    public void sendVerificationEmail(String toEmail, String token) {
        // Construct the verification URL
        String verificationUrl = "http://localhost:8080/api/register/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Account Verification");
        message.setText("Please click the following link to verify your account: " + verificationUrl);

        mailSender.send(message);
    }
}
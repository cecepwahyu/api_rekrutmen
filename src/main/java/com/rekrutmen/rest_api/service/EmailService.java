package com.rekrutmen.rest_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base.url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reset Password OTP");
        String emailBody = "Your Reset Password OTP code is: " + otpCode + "\n\n" +
                "Anda dapat melakukan verifikasi akun melalui link berikut: " + baseUrl + "/account-verification";
        message.setText(emailBody);
        message.setFrom("novian.andika@bpddiy.co.id");

        mailSender.send(message);
    }

    public void sendOtpEmaiVerification(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Account Verification");
        String emailBody = "Your Reset Password OTP code is: " + otpCode + "\n\n" +
                "Anda dapat melakukan verifikasi akun melalui link berikut: " + baseUrl + "/account-verification";
        message.setText(emailBody);
        message.setFrom("novian.andika@bpddiy.co.id");

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
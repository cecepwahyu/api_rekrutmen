package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.*;
import com.rekrutmen.rest_api.service.AuthService;
import com.rekrutmen.rest_api.util.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseWrapper<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        logger.info("Request Data = {no_identitas: {}, email: {}}", resetPasswordRequest.getNoIdentitas(), resetPasswordRequest.getEmail());
        return authService.handleResetPassword(resetPasswordRequest);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ResponseWrapper<Object>> resendOtp(@Valid @RequestBody ResendOtpRequest resendOtpRequest) {
        logger.info("Request Data = {no_identitas: {}, email: {}}", resendOtpRequest.getNoIdentitas(), resendOtpRequest.getEmail());
        return authService.handleResendOtp(resendOtpRequest);
    }

    @PostMapping("/otp-verification")
    public ResponseEntity<ResponseWrapper<Object>> otpVerification(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        logger.info("Request Data = {otp: {}}", otpVerificationRequest.getOtp());
        return authService.handleOtpVerification(otpVerificationRequest);
    }

    @PostMapping("/account-verification")
    public ResponseEntity<ResponseWrapper<Object>> accountVerification(@Valid @RequestBody AccountVerificationRequest accountVerificationRequest) {
        logger.info("Request Data = {otp: {}}", accountVerificationRequest.getOtp());
        return authService.handleAccountVerification(accountVerificationRequest);
    }

    @PostMapping("/get-id-peserta")
    public ResponseEntity<ResponseWrapper<Object>> getIdPeserta(@RequestHeader("Authorization") String authHeader) {
        logger.info("Received request to get ID Peserta");
        return authService.handleGetIdPeserta(authHeader);
    }

    @PutMapping("/update-password")
    public ResponseEntity<ResponseWrapper<Object>> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        logger.info("Received request to update password for email: {}", updatePasswordRequest.getEmail());
        return authService.handleUpdatePassword(updatePasswordRequest);
    }
}

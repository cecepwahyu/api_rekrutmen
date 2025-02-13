package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.*;
import com.rekrutmen.rest_api.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseWrapper<Object>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest,
            HttpServletRequest request
    ) {
        String processName = "RESET_PASSWORD";
        return authService.handleResetPassword(processName, resetPasswordRequest, request);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ResponseWrapper<Object>> resendOtp(
            @Valid @RequestBody ResendOtpRequest resendOtpRequest,
            HttpServletRequest request
    ) {
        String processName = "RESEND_OTP";
        return authService.handleResendOtp(processName, resendOtpRequest, request);
    }

    @PostMapping("/otp-verification")
    public ResponseEntity<ResponseWrapper<Object>> otpVerification(
            @Valid @RequestBody OtpVerificationRequest otpVerificationRequest,
            HttpServletRequest request
    ) {
        String processName = "OTP_VERIFICATION";
        return authService.handleOtpVerification(processName, otpVerificationRequest, request);
    }

    @PostMapping("/account-verification")
    public ResponseEntity<ResponseWrapper<Object>> accountVerification(
            @Valid @RequestBody AccountVerificationRequest accountVerificationRequest,
            HttpServletRequest request
    ) {
        String processName = "ACCOUNT_VERIFICATION";
        return authService.handleAccountVerification(processName, accountVerificationRequest, request);
    }

    @PostMapping("/get-id-peserta")
    public ResponseEntity<ResponseWrapper<Object>> getIdPeserta(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest request
    ) {
        String processName = "GET_ID_PESERTA";
        return authService.handleGetIdPeserta(processName, authHeader, request);
    }

    @PutMapping("/update-password")
    public ResponseEntity<ResponseWrapper<Object>> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest,
            HttpServletRequest request
    ) {
        String processName = "UPDATE_PASSWORD";
        return authService.handleUpdatePassword(processName, updatePasswordRequest, request);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResponseWrapper<Object>> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request
    ) {
        String processName = "CHANGE_PASSWORD";
        return authService.handleChangePassword(processName, changePasswordRequest, request);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ResponseWrapper<Object>> validateToken(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest request
    ) {
        String processName = "VALIDATE_TOKEN";
        return authService.validateToken(processName, authHeader, request);
    }


}

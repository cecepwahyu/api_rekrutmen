package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.OtpVerificationRequest;
import com.rekrutmen.rest_api.dto.ResendOtpRequest;
import com.rekrutmen.rest_api.dto.ResetPasswordRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.service.AuthService;
import com.rekrutmen.rest_api.util.RequestValidatorUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseWrapper<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        logger.info("Request Data = {no_identitas: {}, email: {}}", resetPasswordRequest.getNoIdentitas(), resetPasswordRequest.getEmail());
        //RequestValidatorUtil.validateResetPasswordRequest(resetPasswordRequest);
        return authService.handleResetPassword(resetPasswordRequest);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ResponseWrapper<Object>> resendOtp(@Valid @RequestBody ResendOtpRequest resendOtpRequest) {
        logger.info("Request Data = {no_identitas: {}, email: {}}", resendOtpRequest.getNoIdentitas(), resendOtpRequest.getEmail());
        //RequestValidatorUtil.validateResetPasswordRequest(resetPasswordRequest);
        return authService.handleResendOtp(resendOtpRequest);
    }

    @PostMapping("/otp-verification")
    public ResponseEntity<ResponseWrapper<Object>> otpVerification(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        logger.info("Request Data = {otp: {}}", otpVerificationRequest.getOtp());
        //RequestValidatorUtil.validateOtpVerificationRequest(otpVerificationRequest);
        return authService.handleOtpVerification(otpVerificationRequest);
    }
}

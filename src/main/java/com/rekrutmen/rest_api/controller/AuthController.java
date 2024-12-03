package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.OtpVerificationRequest;
import com.rekrutmen.rest_api.dto.ResetPasswordRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.model.Profile;
import com.rekrutmen.rest_api.service.ProfileService;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseWrapper<Object>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        // Validate email and nik
        Optional<Peserta> pesertaOptional = profileService.validateEmailAndNoIdentitas(
                resetPasswordRequest.getEmail(),
                resetPasswordRequest.getNoIdentitas()
        );
        logger.info("Request Data = {Email: {}, No Identitas: {}}", resetPasswordRequest.getEmail(), resetPasswordRequest.getNoIdentitas());

        if (pesertaOptional.isEmpty()) {
            logger.warn("Email {} or No Identitas {} invalid!", resetPasswordRequest.getEmail(), resetPasswordRequest.getNoIdentitas());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "400",
                    responseCodeUtil.getMessage("400"),
                    "Invalid email or No Identitas"
            ));
        }

        Peserta peserta = pesertaOptional.get();

        // Generate OTP
        String otpCode = generateOtp();
        logger.info("OTP generated: {}", otpCode);

        // Update OTP in the database
        profileService.updateOtp(peserta.getIdPeserta(), otpCode);

        // Prepare response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", resetPasswordRequest.getEmail());
        responseData.put("no_identitas", resetPasswordRequest.getNoIdentitas());
        responseData.put("Your OTP code is", otpCode);

        // Prepare response data
        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    @PostMapping("/otp-verification")
    public ResponseEntity<ResponseWrapper<Object>> otpVerification(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        // Validate OTP code
        Optional<Peserta> pesertaOptional = profileService.validateOtp(
                otpVerificationRequest.getOtp()
        );
        logger.info("Request Data = {OTP code: {}}", otpVerificationRequest.getOtp());

        if (pesertaOptional.isEmpty()) {
            logger.warn("OTP code: {} invalid!", otpVerificationRequest.getOtp());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "400",
                    responseCodeUtil.getMessage("400"),
                    "Invalid OTP code"
            ));
        }

        Peserta peserta = pesertaOptional.get();

        // Generate OTP
        String otpCode = generateOtp();
        logger.info("OTP generated: {}", otpCode);

        // Update OTP in the database
        profileService.updateOtp(peserta.getIdPeserta(), otpCode);

        // Prepare response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("OTP is valid!", otpVerificationRequest.getOtp());

        // Prepare response data
        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}

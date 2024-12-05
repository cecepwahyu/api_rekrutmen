package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.OtpVerificationRequest;
import com.rekrutmen.rest_api.dto.ResetPasswordRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private ProfileService profileService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<Object>> handleResetPassword(ResetPasswordRequest resetPasswordRequest) {
        Optional<Peserta> pesertaOptional = profileService.validateEmailAndNoIdentitas(
                resetPasswordRequest.getEmail(),
                resetPasswordRequest.getNoIdentitas()
        );
        logger.info("Request Data = {Email: {}, No Identitas: {}}", resetPasswordRequest.getEmail(), resetPasswordRequest.getNoIdentitas());

        if (pesertaOptional.isEmpty()) {
            logger.warn("Email {} or No Identitas {} invalid!", resetPasswordRequest.getEmail(), resetPasswordRequest.getNoIdentitas());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Invalid email or No Identitas"
            ));
        }

        Peserta peserta = pesertaOptional.get();
        String otpCode = generateOtp();
        logger.info("OTP generated: {}", otpCode);
        profileService.updateOtp(peserta.getIdPeserta(), otpCode);
        emailService.sendOtpEmail(resetPasswordRequest.getEmail(), otpCode);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", resetPasswordRequest.getEmail());
        responseData.put("no_identitas", resetPasswordRequest.getNoIdentitas());
        responseData.put("Your OTP code is", otpCode);

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> handleOtpVerification(OtpVerificationRequest otpVerificationRequest) {
        Optional<Peserta> pesertaOptional = profileService.validateOtp(otpVerificationRequest.getOtp());
        logger.info("Request Data = {OTP code: {}}", otpVerificationRequest.getOtp());

        if (pesertaOptional.isEmpty()) {
            logger.warn("OTP code: {} invalid!", otpVerificationRequest.getOtp());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Invalid OTP code"
            ));
        }

        Peserta peserta = pesertaOptional.get();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("OTP is valid!", otpVerificationRequest.getOtp());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    private String generateOtp() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }
}

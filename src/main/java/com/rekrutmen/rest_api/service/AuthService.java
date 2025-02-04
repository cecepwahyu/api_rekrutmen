package com.rekrutmen.rest_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rekrutmen.rest_api.dto.*;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.repository.PesertaRepository;
import com.rekrutmen.rest_api.util.JwtUtil;
import com.rekrutmen.rest_api.util.OtpUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final String PASSWORD_MIN_LENGTH_REGEX = ".{8,}";
    private static final String PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String PASSWORD_LOWERCASE_REGEX = ".*[a-z].*";
    private static final String PASSWORD_DIGIT_REGEX = ".*\\d.*";
    private static final String PASSWORD_SPECIAL_CHAR_REGEX = ".*[^a-zA-Z0-9].*";

    @Autowired
    private ProfileService profileService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PesertaRepository pesertaRepository;

    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_MIN_LENGTH_REGEX, password) &&
                Pattern.matches(PASSWORD_UPPERCASE_REGEX, password) &&
                Pattern.matches(PASSWORD_LOWERCASE_REGEX, password) &&
                Pattern.matches(PASSWORD_DIGIT_REGEX, password) &&
                Pattern.matches(PASSWORD_SPECIAL_CHAR_REGEX, password);
    }

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
        String otpCode = OtpUtil.generateOtp();
        LocalDateTime updatedAt = LocalDateTime.now();
        logger.info("OTP generated: {}", otpCode);
        profileService.updateOtp(peserta.getIdPeserta().intValue(), otpCode, updatedAt);
        emailService.sendOtpEmail(resetPasswordRequest.getEmail(), otpCode);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", resetPasswordRequest.getEmail());
        responseData.put("no_identitas", resetPasswordRequest.getNoIdentitas());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> handleResendOtp(ResendOtpRequest resendOtpRequest) {
        Optional<Peserta> pesertaOptional = profileService.validateEmailAndNoIdentitas(
                resendOtpRequest.getEmail(),
                resendOtpRequest.getNoIdentitas()
        );
        logger.info("Request Data = {Email: {}, No Identitas: {}}", resendOtpRequest.getEmail(), resendOtpRequest.getNoIdentitas());

        if (pesertaOptional.isEmpty()) {
            logger.warn("Email {} or No Identitas {} invalid!", resendOtpRequest.getEmail(), resendOtpRequest.getNoIdentitas());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Invalid email or No Identitas"
            ));
        }

        Peserta peserta = pesertaOptional.get();

        // Check if the last OTP generation was less than 30 seconds ago
        LocalDateTime lastOtpUpdatedAt = profileService.updateOtpUpdatedAt(peserta.getIdPeserta(), peserta.getOtpUpdatedAt());
        LocalDateTime now = LocalDateTime.now();
        if (lastOtpUpdatedAt != null && Duration.between(lastOtpUpdatedAt, now).getSeconds() < 30) {
            long secondsRemaining = 30 - Duration.between(lastOtpUpdatedAt, now).getSeconds();
            logger.warn("OTP resend request blocked for {}. Please wait {} seconds.", resendOtpRequest.getEmail(), secondsRemaining);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("429"),
                    "Please wait " + secondsRemaining + " seconds before resending OTP.",
                    null
            ));
        }

        // Generate and send OTP
        String otpCode = OtpUtil.generateOtp();
        LocalDateTime updatedAt = LocalDateTime.now();
        logger.info("OTP generated: {}", otpCode);
        profileService.updateOtp(peserta.getIdPeserta().intValue(), otpCode, updatedAt);
        emailService.sendOtpEmail(resendOtpRequest.getEmail(), otpCode);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", resendOtpRequest.getEmail());
        responseData.put("no_identitas", resendOtpRequest.getNoIdentitas());

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

    public ResponseEntity<ResponseWrapper<Object>> handleAccountVerification(AccountVerificationRequest accountVerificationRequest) {
        Optional<Peserta> pesertaOptional = profileService.validateOtp(accountVerificationRequest.getOtp());
        logger.info("Request Data = {OTP code: {}}", accountVerificationRequest.getOtp());

        if (pesertaOptional.isEmpty()) {
            logger.warn("OTP code: {} invalid!", accountVerificationRequest.getOtp());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Invalid OTP code"
            ));
        }

        Peserta peserta = pesertaOptional.get();

        // Update is_active to true
        peserta.setIsActive(true);
        peserta.setOtp(null);
        pesertaRepository.save(peserta);

        logger.info("Account verified successfully for Peserta ID: {}", peserta.getIdPeserta());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("OTP is valid!", accountVerificationRequest.getOtp());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> handleGetIdPeserta(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Invalid Authorization Header");
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "401", "Invalid Authorization Header", null
            ));
        }

        String token = authHeader.substring(7);

        try {
            // Decode the token for additional details
            Map<String, String> decodedToken = JwtUtil.decodeToken(token);
            logger.info("Decoded Token Details = {header: {}, payload: {}, signature: {}}",
                    decodedToken.get("header"), decodedToken.get("payload"), decodedToken.get("signature"));

            // Parse payload JSON to retrieve idPeserta and email
            String payload = decodedToken.get("payload");
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payloadData = objectMapper.readValue(payload, Map.class);

            String idPeserta = payloadData.get("idPeserta") != null ? payloadData.get("idPeserta").toString() : null;
            String email = payloadData.get("email") != null ? payloadData.get("email").toString() : null;

            if (idPeserta == null || email == null) {
                logger.warn("ID Peserta or Email not found in token");
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                        "400", "ID Peserta or Email not found in token", null
                ));
            }

            // Log the extracted information
            logger.info("Extracted Data from Token = {idPeserta: {}, email: {}}", idPeserta, email);

            return ResponseEntity.ok(new ResponseWrapper<>(
                    "000", "Success", Map.of("idPeserta", idPeserta, "email", email)
            ));
        } catch (Exception e) {
            logger.error("Error extracting data from token: {}", e.getMessage(), e);
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    "401", "Invalid or expired token", null
            ));
        }
    }

    public ResponseEntity<ResponseWrapper<Object>> handleUpdatePassword(UpdatePasswordRequest updatePasswordRequest) {
        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
            logger.warn("New password and confirm password do not match");
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Passwords do not match",
                    null
            ));
        }

        if (!isValidPassword(updatePasswordRequest.getNewPassword())) {
            logger.warn("New password does not meet security requirements");
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character",
                    null
            ));
        }

        Optional<Peserta> pesertaOptional = pesertaRepository.findByEmail(updatePasswordRequest.getEmail());
        if (pesertaOptional.isEmpty()) {
            logger.warn("No user found with email: {}", updatePasswordRequest.getEmail());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("404"),
                    "User not found",
                    null
            ));
        }

        Peserta peserta = pesertaOptional.get();
        String encryptedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        peserta.setPassword(encryptedPassword);
        peserta.setUpdatedAt(LocalDateTime.now());
        pesertaRepository.save(peserta);

        logger.info("Password updated successfully for email: {}", updatePasswordRequest.getEmail());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Password updated successfully",
                null
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> handleChangePassword(ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            logger.warn("New password and confirm password do not match for email: {}", changePasswordRequest.getEmail());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "New password and confirm password do not match",
                    null
            ));
        }

        if (!isValidPassword(changePasswordRequest.getNewPassword())) {
            logger.warn("New password does not meet security requirements for email: {}", changePasswordRequest.getEmail());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character",
                    null
            ));
        }

        Optional<Peserta> pesertaOptional = pesertaRepository.findByEmail(changePasswordRequest.getEmail());
        if (pesertaOptional.isEmpty()) {
            logger.warn("No user found with email: {}", changePasswordRequest.getEmail());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("404"),
                    "User not found",
                    null
            ));
        }

        Peserta peserta = pesertaOptional.get();

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), peserta.getPassword())) {
            logger.warn("Current password does not match for email: {}", changePasswordRequest.getEmail());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Current password is incorrect",
                    null
            ));
        }

        String encryptedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        peserta.setPassword(encryptedPassword);
        peserta.setUpdatedAt(LocalDateTime.now());
        pesertaRepository.save(peserta);

        logger.info("Password changed successfully for email: {}", changePasswordRequest.getEmail());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Password changed successfully",
                null
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Invalid Authorization Header");
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "401", "Invalid Authorization Header", null
            ));
        }

        String token = authHeader.substring(7); // Extract the token

        try {
            // Validate the token and check expiration
            if (!JwtUtil.isTokenValid(token)) {
                logger.warn("Token is expired or invalid");
                return ResponseEntity.status(401).body(new ResponseWrapper<>(
                        "401", "Token is expired or invalid", null
                ));
            }

            // Parse the token to get claims
            Claims claims = JwtUtil.parseToken(token);

            // Optionally retrieve custom claims (like user ID, email, etc.)
            String subject = claims.getSubject(); // Usually the user identifier
            Date expiration = claims.getExpiration();

            // Return success response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("subject", subject);
            responseData.put("expiration", expiration);

            logger.info("Token is valid for subject: {}", subject);

            return ResponseEntity.ok(new ResponseWrapper<>(
                    "000", "Token is valid", responseData
            ));
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage(), e);
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    "401", "Invalid or expired token", null
            ));
        }
    }


}

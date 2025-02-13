package com.rekrutmen.rest_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rekrutmen.rest_api.dto.*;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.repository.PesertaRepository;
import com.rekrutmen.rest_api.util.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final String PASSWORD_MIN_LENGTH_REGEX = ".{8,}";
    private static final String PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String PASSWORD_LOWERCASE_REGEX = ".*[a-z].*";
    private static final String PASSWORD_DIGIT_REGEX = ".*\\d.*";
    private static final String PASSWORD_SPECIAL_CHAR_REGEX = ".*[^a-zA-Z0-9].*";

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION_MS = 5 * 60 * 1000; // 5 minutes
    private static final long ATTEMPT_RESET_DURATION_MS = 5 * 60 * 1000; // Reset attempts every 5 minutes

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

    private final Map<String, List<Long>> correctAttempts = new ConcurrentHashMap<>();
    private final Map<String, List<Long>> allAttempts = new ConcurrentHashMap<>();
    private final Map<String, List<Long>> incorrectAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> lockedAccounts = new ConcurrentHashMap<>();

    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_MIN_LENGTH_REGEX, password) &&
                Pattern.matches(PASSWORD_UPPERCASE_REGEX, password) &&
                Pattern.matches(PASSWORD_LOWERCASE_REGEX, password) &&
                Pattern.matches(PASSWORD_DIGIT_REGEX, password) &&
                Pattern.matches(PASSWORD_SPECIAL_CHAR_REGEX, password);
    }

    public ResponseEntity<ResponseWrapper<Object>> handleResetPassword(String processName, ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {

        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        String email = resetPasswordRequest.getEmail();
        String noIdentitas = resetPasswordRequest.getNoIdentitas();
        long currentTime = System.currentTimeMillis();

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(resetPasswordRequest)
        ));

        // Check if the account is locked
        if (isAccountLocked(email)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("403"),
                    responseCodeUtil.getMessage("403"),
                    Map.of("message", "Too many reset attempts. Please wait 5 minutes before trying again.")
            ));
            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("403"),
                    responseCodeUtil.getMessage("403"),
                    Map.of("message", "Too many reset attempts. Please wait 5 minutes before trying again.")
            ));
        }

        // Track total attempts (both success & failure)
        trackAttempt(email, currentTime);

        // If the user reaches the max attempts, lock the account
        if (hasExceededAttemptLimit(email)) {
            lockedAccounts.put(email, currentTime + LOCKOUT_DURATION_MS);
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("403"),
                    responseCodeUtil.getMessage("403"),
                    Map.of("message", "Too many reset attempts. Please wait 5 minutes before trying again.")
            ));
            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("403"),
                    responseCodeUtil.getMessage("403"),
                    Map.of("message", "Too many reset attempts. Please wait 5 minutes before trying again.")
            ));
        }

        Optional<Peserta> pesertaOptional = profileService.validateEmailAndNoIdentitas(email, noIdentitas);

        if (pesertaOptional.isEmpty()) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Invalid email or No Identitas")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Invalid email or No Identitas"
            ));
        }

        Peserta peserta = pesertaOptional.get();
        String otpCode = OtpUtil.generateOtp();
        LocalDateTime updatedAt = LocalDateTime.now();
        profileService.updateOtp(peserta.getIdPeserta().intValue(), otpCode, updatedAt);
        emailService.sendOtpEmail(email, otpCode);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", email);
        responseData.put("no_identitas", MaskingUtil.maskPassword(noIdentitas));

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> handleResendOtp(String processName, ResendOtpRequest resendOtpRequest, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        Optional<Peserta> pesertaOptional = profileService.validateEmailAndNoIdentitas(
                resendOtpRequest.getEmail(),
                resendOtpRequest.getNoIdentitas()
        );
        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(resendOtpRequest)
        ));

        if (pesertaOptional.isEmpty()) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Invalid email or No Identitas")
            ));
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
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("429"),
                    "OTP resend request blocked. Please wait " + secondsRemaining + " seconds.",
                    Map.of("email", resendOtpRequest.getEmail(), "secondsRemaining", secondsRemaining)
            ));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("429"),
                    "Please wait " + secondsRemaining + " seconds before resending OTP.",
                    null
            ));
        }

        // Generate and send OTP
        String otpCode = OtpUtil.generateOtp();
        LocalDateTime updatedAt = LocalDateTime.now();
        profileService.updateOtp(peserta.getIdPeserta().intValue(), otpCode, updatedAt);
        emailService.sendOtpEmail(resendOtpRequest.getEmail(), otpCode);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", resendOtpRequest.getEmail());
        responseData.put("no_identitas", resendOtpRequest.getNoIdentitas());

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> handleOtpVerification(String processName, OtpVerificationRequest otpVerificationRequest, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        Optional<Peserta> pesertaOptional = profileService.validateOtp(otpVerificationRequest.getOtp());
        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(otpVerificationRequest)
        ));

        if (pesertaOptional.isEmpty()) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Invalid OTP code")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Invalid OTP code"
            ));
        }

        Peserta peserta = pesertaOptional.get();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("OTP is valid!", otpVerificationRequest.getOtp());

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> handleAccountVerification(String processName, AccountVerificationRequest accountVerificationRequest, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        Optional<Peserta> pesertaOptional = profileService.validateOtp(accountVerificationRequest.getOtp());
        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(accountVerificationRequest)
        ));

        if (pesertaOptional.isEmpty()) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Invalid OTP code")
            ));
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

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("OTP is valid!", accountVerificationRequest.getOtp());

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> handleGetIdPeserta(String processName, String authHeader, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                Map.of("message", "No request body provided")
        ));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("401"),
                    responseCodeUtil.getMessage("401"),
                    Map.of("message", "Invalid Authorization Header")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("401"),
                    "Invalid Authorization Header",
                    null
            ));
        }

        String token = authHeader.substring(7);

        try {
            // Decode the token for additional details
            Map<String, String> decodedToken = JwtUtil.decodeToken(token);

            // Parse payload JSON to retrieve idPeserta and email
            String payload = decodedToken.get("payload");
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payloadData = objectMapper.readValue(payload, Map.class);

            String idPeserta = payloadData.get("idPeserta") != null ? payloadData.get("idPeserta").toString() : null;
            String email = payloadData.get("email") != null ? payloadData.get("email").toString() : null;

            if (idPeserta == null || email == null) {
                logger.warn(LoggerUtil.formatLog(
                        jobId, ip, "R", processName,
                        responseCodeUtil.getCode("400"),
                        responseCodeUtil.getMessage("400"),
                        Map.of("message", "ID Peserta or Email not found in token")
                ));
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        "ID Peserta or Email not found in token",
                        null
                ));
            }

            logger.info(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    Map.of("idPeserta", idPeserta, "email", email, "message", "Id Peserta found in token")
            ));
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    Map.of("idPeserta", idPeserta, "email", email)
            ));
        } catch (Exception e) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("401"),
                    responseCodeUtil.getMessage("401"),
                    Map.of("message", "Invalid or expired token")
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("401"),
                    "Invalid or expired token",
                    null
            ));
        }
    }

    public ResponseEntity<ResponseWrapper<Object>> handleUpdatePassword(String processName, UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(updatePasswordRequest)
        ));

        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "New password and confirm password do not match")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Passwords do not match",
                    null
            ));
        }

        if (!isValidPassword(updatePasswordRequest.getNewPassword())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character",
                    null
            ));
        }

        Optional<Peserta> pesertaOptional = pesertaRepository.findByEmail(updatePasswordRequest.getEmail());
        if (pesertaOptional.isEmpty()) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "User not found")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "User not found",
                    null
            ));
        }

        Peserta peserta = pesertaOptional.get();
        String encryptedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        peserta.setPassword(encryptedPassword);
        peserta.setUpdatedAt(LocalDateTime.now());
        pesertaRepository.save(peserta);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                Map.of("message", "Password updated successfully")
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Password updated successfully",
                null
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> handleChangePassword(String processName, ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(changePasswordRequest)
        ));

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "New password and confirm password do not match")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "New password and confirm password do not match",
                    null
            ));
        }

        if (!isValidPassword(changePasswordRequest.getNewPassword())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character",
                    null
            ));
        }

        Optional<Peserta> pesertaOptional = pesertaRepository.findByEmail(changePasswordRequest.getEmail());
        if (pesertaOptional.isEmpty()) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "User not found")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "User not found",
                    null
            ));
        }

        Peserta peserta = pesertaOptional.get();

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), peserta.getPassword())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Current password is incorrect")
            ));
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

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                Map.of("message", "Password changed successfully")
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Password changed successfully",
                null
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> validateToken(String processName, String authHeader, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                Map.of("message", "No request body provided")
        ));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("401"),
                    responseCodeUtil.getMessage("401"),
                    Map.of("message", "Invalid Authorization Header")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("401"),
                    "Invalid Authorization Header",
                    null
            ));
        }

        String token = authHeader.substring(7); // Extract the token

        try {
            // Validate the token and check expiration
            if (!JwtUtil.isTokenValid(token)) {
                logger.warn(LoggerUtil.formatLog(
                        jobId, ip, "R", processName,
                        responseCodeUtil.getCode("401"),
                        responseCodeUtil.getMessage("401"),
                        Map.of("message", "Token is expired or invalid")
                ));
                return ResponseEntity.status(401).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("401"),
                        "Token is expired or invalid",
                        null
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

            logger.info(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    Map.of("message", "Token is valid")
            ));
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    "Token is valid",
                    responseData
            ));
        } catch (Exception e) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("401"),
                    responseCodeUtil.getMessage("401"),
                    Map.of("message", "Invalid or expired token")
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("401"),
                    "Invalid or expired token",
                    null
            ));
        }
    }

    private boolean isAccountLocked(String email) {
        return lockedAccounts.containsKey(email) && lockedAccounts.get(email) > System.currentTimeMillis();
    }

    /**
     * Tracks all login/reset attempts (successful and failed)
     */
    private void trackAttempt(String email, long currentTime) {
        allAttempts.putIfAbsent(email, new ArrayList<>());

        List<Long> attempts = allAttempts.get(email);
        attempts.add(currentTime);

        // Remove attempts older than the reset duration (5 minutes)
        attempts.removeIf(timestamp -> timestamp < currentTime - ATTEMPT_RESET_DURATION_MS);

        // If attempts reach MAX_ATTEMPTS, lock the account
        if (attempts.size() >= MAX_ATTEMPTS) {
            lockedAccounts.put(email, currentTime + LOCKOUT_DURATION_MS);
            allAttempts.remove(email); // Clear all attempts after lockout
        }
    }

    private boolean hasExceededLimit(String email, boolean isCorrect) {
        Map<String, List<Long>> attemptMap = isCorrect ? correctAttempts : incorrectAttempts;
        return attemptMap.containsKey(email) && attemptMap.get(email).size() >= MAX_ATTEMPTS;
    }

    private boolean hasExceededIncorrectLimit(String email) {
        return incorrectAttempts.containsKey(email) && incorrectAttempts.get(email).size() >= 3;
    }

    /**
     * Checks if the user has exceeded the total attempt limit
     */
    private boolean hasExceededAttemptLimit(String email) {
        return allAttempts.containsKey(email) && allAttempts.get(email).size() >= MAX_ATTEMPTS;
    }

}

package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.LoginRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.repository.PesertaRepository;
import com.rekrutmen.rest_api.util.JwtUtil;
import com.rekrutmen.rest_api.util.LoggerUtil;
import com.rekrutmen.rest_api.util.MaskingUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    // Email & Password REGEX
    private static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
    private static final String PASSWORD_MIN_LENGTH_REGEX = ".{8,}";
    private static final String PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String PASSWORD_LOWERCASE_REGEX = ".*[a-z].*";
    private static final String PASSWORD_DIGIT_REGEX = ".*\\d.*";
    private static final String PASSWORD_SPECIAL_CHAR_REGEX = ".*[^a-zA-Z0-9].*";

    // Login Limit Data
    private static final int MAX_ATTEMPTS = 3;
    private static final long ATTEMPT_WINDOW_MS = 2 * 60 * 1000; // 2 minutes
    private static final long LOCKOUT_DURATION_MS = 5 * 60 * 1000; // 5 minutes

    private final PesertaRepository pesertaRepository;
    private final ResponseCodeUtil responseCodeUtil;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, List<Long>> loginAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> lockedAccounts = new ConcurrentHashMap<>();

    @Autowired
    public LoginService(PesertaRepository pesertaRepository, ResponseCodeUtil responseCodeUtil, PasswordEncoder passwordEncoder) {
        this.pesertaRepository = pesertaRepository;
        this.responseCodeUtil = responseCodeUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private LogService logService;

    public ResponseEntity<ResponseWrapper<Object>> handleLogin(String processName, LoginRequest loginRequest, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        String jsonRequestPayload = LoggerUtil.convertObjectToJson(loginRequest);

        // Insert log into database
        logService.log(
                jobId, // Job ID
                ip, // IP Address
                "S", // Signal (Send)
                "1", // Device (Client)
                processName, // Process Name
                jsonRequestPayload, // Message
                loginRequest.getEmail(), // Username
                1 // Sequence
        );

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(loginRequest)
        ));

        // Validate email format
        if (!isValidEmail(loginRequest.getEmail())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Invalid email format", "email", loginRequest.getEmail())
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Invalid email format"
            ));
        }

        // Validate password requirements
        if (!isValidPassword(loginRequest.getPassword())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character"
            ));
        }

        String email = loginRequest.getEmail();
        long currentTime = System.currentTimeMillis();

        if (isAccountLocked(email)) {
            long timeLeft = (lockedAccounts.get(email) - currentTime) / 1000;

            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("403"),
                    responseCodeUtil.getMessage("403"),
                    Map.of("message", "Too many failed login attempts. Try again in " + timeLeft + " seconds.")
            ));

            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("403"),
                    responseCodeUtil.getMessage("403"),
                    Map.of("message", "Too many failed login attempts. Try again in " + timeLeft + " seconds.")
            ));
        }


        trackLoginAttempt(email, currentTime);

        Optional<Peserta> optionalUser = pesertaRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isPresent()) {
            Peserta existingUser = optionalUser.get();

            if (passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
                // Add idPeserta to token claims
                Map<String, Object> claims = new HashMap<>();
                claims.put("idPeserta", existingUser.getIdPeserta());
                claims.put("email", loginRequest.getEmail());

                // Generate JWT token with custom claims
                String token = JwtUtil.generateTokenWithClaims(claims);

                // Update the token in the database
                existingUser.setToken(token);
                existingUser.setTokenUpdatedAt(LocalDateTime.now());
                pesertaRepository.save(existingUser);

                // Prepare response data
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("idPeserta", existingUser.getIdPeserta());
                responseData.put("token", token);
                responseData.put("email", loginRequest.getEmail());
                responseData.put("isActive", existingUser.getIsActive());

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
            } else {
                logger.warn(LoggerUtil.formatLog(
                        jobId, ip, "R", processName,
                        responseCodeUtil.getCode("400"),
                        responseCodeUtil.getMessage("400"),
                        Map.of("message", "Invalid password", "email", loginRequest.getEmail())
                ));
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        responseCodeUtil.getMessage("400"),
                        "Invalid password"
                ));
            }
        } else {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("401"),
                    responseCodeUtil.getMessage("401"),
                    Map.of("message", "No user found", "email", loginRequest.getEmail())
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("401"),
                    responseCodeUtil.getMessage("401"),
                    "No user found"
            ));
        }
    }

    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_MIN_LENGTH_REGEX, password) &&
                Pattern.matches(PASSWORD_UPPERCASE_REGEX, password) &&
                Pattern.matches(PASSWORD_LOWERCASE_REGEX, password) &&
                Pattern.matches(PASSWORD_DIGIT_REGEX, password) &&
                Pattern.matches(PASSWORD_SPECIAL_CHAR_REGEX, password);
    }

    private boolean isAccountLocked(String email) {
        return lockedAccounts.containsKey(email) && lockedAccounts.get(email) > System.currentTimeMillis();
    }

    private void trackLoginAttempt(String email, long currentTime) {
        loginAttempts.putIfAbsent(email, new ArrayList<>());
        List<Long> attempts = loginAttempts.get(email);
        attempts.add(currentTime);
        attempts.removeIf(timestamp -> timestamp < currentTime - ATTEMPT_WINDOW_MS);
        if (attempts.size() >= MAX_ATTEMPTS) {
            lockedAccounts.put(email, currentTime + LOCKOUT_DURATION_MS);
            loginAttempts.remove(email);
        }
    }

}


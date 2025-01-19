package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.LoginRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.repository.PesertaRepository;
import com.rekrutmen.rest_api.util.JwtUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    private static final String EMAIL_REGEX = "^[\\w-\\.+]+@[\\w-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_MIN_LENGTH_REGEX = ".{8,}";
    private static final String PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String PASSWORD_LOWERCASE_REGEX = ".*[a-z].*";
    private static final String PASSWORD_DIGIT_REGEX = ".*\\d.*";
    private static final String PASSWORD_SPECIAL_CHAR_REGEX = ".*[^a-zA-Z0-9].*";

    private final PesertaRepository pesertaRepository;
    private final ResponseCodeUtil responseCodeUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(PesertaRepository pesertaRepository, ResponseCodeUtil responseCodeUtil, PasswordEncoder passwordEncoder) {
        this.pesertaRepository = pesertaRepository;
        this.responseCodeUtil = responseCodeUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<ResponseWrapper<Object>> handleLogin(LoginRequest loginRequest) {
        logger.info("Request Data: {email: {}, password: {}}", loginRequest.getEmail(), loginRequest.getPassword());

        // Validate email format
        if (!isValidEmail(loginRequest.getEmail())) {
            logger.warn("Invalid email format: {}", loginRequest.getEmail());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Invalid email format"
            ));
        }

        // Validate password requirements
        if (!isValidPassword(loginRequest.getPassword())) {
            logger.warn("Password does not meet requirements");
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character"
            ));
        }

        Optional<Peserta> optionalUser = pesertaRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isPresent()) {
            Peserta existingUser = optionalUser.get();
            logger.info("User found for email: {}", loginRequest.getEmail());

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

                logger.info("Response Data: {responseCode: {}, responseMessage: {}, data:{{email: {}, token: {}, idPeserta: {}, isActive: {}}}}",
                        responseCodeUtil.getCode("000"), responseCodeUtil.getMessage("000"),
                        loginRequest.getEmail(), token, existingUser.getIdPeserta(), existingUser.getIsActive());

                return ResponseEntity.ok(new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        responseCodeUtil.getMessage("000"),
                        responseData
                ));
            } else {
                logger.warn("Invalid password for email: {}", loginRequest.getEmail());
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        responseCodeUtil.getMessage("400"),
                        "Invalid password"
                ));
            }
        } else {
            logger.warn("No user found for email: {}", loginRequest.getEmail());
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
}


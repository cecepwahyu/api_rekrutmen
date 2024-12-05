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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

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
        logger.info("Login request received for email: {}", loginRequest.getEmail());

        Optional<Peserta> optionalUser = pesertaRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isPresent()) {
            Peserta existingUser = optionalUser.get();
            logger.info("User found for email: {}", loginRequest.getEmail());

            if (passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
                // Generate JWT token
                String token = JwtUtil.generateToken(loginRequest.getEmail());

                // Update the token in the database
                existingUser.setToken(token);
                pesertaRepository.save(existingUser);

                // Prepare response data
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                responseData.put("email", loginRequest.getEmail());

                logger.info("Login successful for email: {}. Token generated: {}", loginRequest.getEmail(), token);

                return ResponseEntity.ok(new ResponseWrapper<>(
                        "000",
                        responseCodeUtil.getMessage("000"),
                        responseData
                ));
            } else {
                logger.warn("Invalid password for email: {}", loginRequest.getEmail());
                return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                        "400",
                        responseCodeUtil.getMessage("400"),
                        "Invalid password"
                ));
            }
        } else {
            logger.warn("No user found for email: {}", loginRequest.getEmail());
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    "401",
                    responseCodeUtil.getMessage("401"),
                    "No user found"
            ));
        }
    }
}

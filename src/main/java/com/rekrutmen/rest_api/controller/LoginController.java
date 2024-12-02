package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.LoginRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.service.LoginService;
import com.rekrutmen.rest_api.service.PesertaService;
import com.rekrutmen.rest_api.util.JwtUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private PesertaService pesertaService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<ResponseWrapper<Object>> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for email: {}", loginRequest.getEmail());

        // Retrieve user by email
        Optional<Peserta> optionalUser = pesertaService.getUserByEmail(loginRequest.getEmail());

        if (optionalUser.isPresent()) {
            Peserta existingUser = optionalUser.get();
            logger.info("User found for email: {}", loginRequest.getEmail());

            if (passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
                // Generate JWT token
                String token = JwtUtil.generateToken(loginRequest.getEmail());

                // Update the token in the database
                existingUser.setToken(token);
                pesertaService.updateUser(existingUser);

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
            }
        } else {
            logger.warn("No user found for email: {}", loginRequest.getEmail());
        }

        // Return unauthorized response
        logger.info("Login failed for email: {}", loginRequest.getEmail());
        return ResponseEntity.status(401).body(new ResponseWrapper<>(
                "401",
                responseCodeUtil.getMessage("401"),
                "Invalid email or password"
        ));
    }
}

package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.controller.LoginController;
import com.rekrutmen.rest_api.dto.RegisterRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.util.MaskingUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private PesertaService pesertaService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<ResponseWrapper<Object>> handleRegister(RegisterRequest registerRequest) {
        // Check if username or email is already taken
        if (pesertaService.isUsernameTaken(registerRequest.getUsername())) {
            logger.warn("Username: {} already registered!", registerRequest.getUsername());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Username already exist"
            ));
        }

        if (pesertaService.isEmailTaken(registerRequest.getEmail())) {
            logger.warn("Email: {} already registered!", registerRequest.getEmail());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Email already registered"
            ));
        }

        if (pesertaService.isNoIdentitasExist(registerRequest.getNoIdentitas())) {
            logger.warn("No Identitas: {} already registered!", registerRequest.getNoIdentitas());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "NIK already registered"
            ));
        }

        // Encrypt the password
        String encryptedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // Create and save new user
        Peserta newUser = new Peserta();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(registerRequest.getEmail());
        newUser.setNoIdentitas(registerRequest.getNoIdentitas());
        newUser.setFlgStatus('0');
        pesertaService.registerUser(newUser);

        // Generate a verification token
        String token = UUID.randomUUID().toString();
        newUser.setToken(token);
        pesertaService.saveUser(newUser);

        // Send verification email
        emailService.sendVerificationEmail(newUser.getEmail(), token);

        logger.info(
                "Response Data = {\"responseCode\": \"{}\", \"responseMessage\": \"{}\", \"data\": {\"username\": \"{}\", \"no_identitas\": \"{}\", \"email\": \"{}\", \"password\": \"{}\"}}",
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                registerRequest.getUsername(),
                registerRequest.getNoIdentitas(),
                registerRequest.getEmail(),
                MaskingUtil.maskPassword(registerRequest.getPassword())
        );

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("username", registerRequest.getUsername());
        responseData.put("no_identitas", registerRequest.getNoIdentitas());
        responseData.put("email", registerRequest.getEmail());
        responseData.put("password", registerRequest.getPassword());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }
}

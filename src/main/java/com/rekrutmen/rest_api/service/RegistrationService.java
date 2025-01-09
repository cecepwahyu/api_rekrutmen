package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.controller.LoginController;
import com.rekrutmen.rest_api.dto.RegisterRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.util.MaskingUtil;
import com.rekrutmen.rest_api.util.OtpUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
        newUser.setNama(registerRequest.getNama());
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(registerRequest.getEmail());
        newUser.setNoIdentitas(registerRequest.getNoIdentitas());
        newUser.setFlgStatus('0');
        pesertaService.registerUser(newUser);

        // Generate a verification OTP
        String otpCode = OtpUtil.generateOtp();
        newUser.setOtp(otpCode);
        pesertaService.saveUser(newUser);

        // Send verification email
        emailService.sendOtpEmaiVerification(newUser.getEmail(), otpCode);

        logger.info(
                "Response Data = {\"responseCode\": \"{}\", \"responseMessage\": \"{}\", \"data\": {\"nama\": \"{}\", \"username\": \"{}\", \"no_identitas\": \"{}\", \"email\": \"{}\", \"password\": \"{}\", \"OTP\": \"{}\"}}",
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                registerRequest.getNama(),
                registerRequest.getUsername(),
                registerRequest.getNoIdentitas(),
                registerRequest.getEmail(),
                MaskingUtil.maskPassword(registerRequest.getPassword()),
                otpCode
        );

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("nama", registerRequest.getNama());
        responseData.put("username", registerRequest.getUsername());
        responseData.put("no_identitas", registerRequest.getNoIdentitas());
        responseData.put("email", registerRequest.getEmail());
        responseData.put("password", registerRequest.getPassword());
        responseData.put("otp", otpCode);

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }
}

package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.controller.LoginController;
import com.rekrutmen.rest_api.dto.RegisterRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.util.LoggerUtil;
import com.rekrutmen.rest_api.util.MaskingUtil;
import com.rekrutmen.rest_api.util.OtpUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static final int NIK_LENGTH = 16;
    private static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
    private static final String PASSWORD_MIN_LENGTH_REGEX = ".{8,}";
    private static final String PASSWORD_UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String PASSWORD_LOWERCASE_REGEX = ".*[a-z].*";
    private static final String PASSWORD_DIGIT_REGEX = ".*\\d.*";
    private static final String PASSWORD_SPECIAL_CHAR_REGEX = ".*[^a-zA-Z0-9].*";

    @Autowired
    private PesertaService pesertaService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<ResponseWrapper<Object>> handleRegister(String processName, RegisterRequest registerRequest, HttpServletRequest request) {

        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        // Log request
        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(registerRequest)
        ));

        // Validate NIK/No Identitas length
        if (!isValidNIK(registerRequest.getNoIdentitas())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "No Identitas must be exactly 16 characters", "no_identitas", registerRequest.getNoIdentitas())
            ));

            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "No Identitas must be exactly 16 characters"
            ));
        }

        // Validate email format
        if (!isValidEmail(registerRequest.getEmail())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Invalid email format", "email", registerRequest.getEmail())
            ));

            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Email format is invalid"
            ));
        }

        // Validate password requirements
        if (!isValidPassword(registerRequest.getPassword())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Password must have at least 8 characters, 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character")
            ));

            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Password must have at least 8 characters, 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character"
            ));
        }

        if (pesertaService.isEmailTaken(registerRequest.getEmail())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Email already registered", "email", registerRequest.getEmail())
            ));

            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Email already registered"
            ));
        }

        if (pesertaService.isNoIdentitasExist(registerRequest.getNoIdentitas())) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "No Identitas/NIK already registered", "no_identitas", registerRequest.getNoIdentitas())
            ));

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

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("nama", registerRequest.getNama());
        responseData.put("no_identitas", MaskingUtil.maskPassword(registerRequest.getNoIdentitas()));
        responseData.put("email", registerRequest.getEmail());
        responseData.put("password", MaskingUtil.maskPassword(registerRequest.getPassword()));

        // Log response
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

    private boolean isValidNIK(String nik) {
        return nik != null && nik.length() == NIK_LENGTH && nik.matches("\\d+");
    }

    private boolean isValidPassword(String password) {
        return password != null &&
                Pattern.matches(PASSWORD_MIN_LENGTH_REGEX, password) &&
                Pattern.matches(PASSWORD_UPPERCASE_REGEX, password) &&
                Pattern.matches(PASSWORD_LOWERCASE_REGEX, password) &&
                Pattern.matches(PASSWORD_DIGIT_REGEX, password) &&
                Pattern.matches(PASSWORD_SPECIAL_CHAR_REGEX, password);
    }

    private boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }
}

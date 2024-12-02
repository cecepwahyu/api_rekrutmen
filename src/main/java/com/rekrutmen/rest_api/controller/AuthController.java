package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResetPasswordRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.model.Profile;
import com.rekrutmen.rest_api.service.ProfileService;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseWrapper<Object>> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        // Validate email and nik
        Optional<Peserta> peserta = profileService.validateEmailAndNoIdentitas(resetPasswordRequest.getEmail(), resetPasswordRequest.getNoIdentitas());
        if (peserta.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "400",
                    responseCodeUtil.getMessage("400"),
                    "Invalid email or NIK"
            ));
        }

        // Generate OTP
        String otpCode = generateOtp();

        // Prepare response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", resetPasswordRequest.getEmail());
        responseData.put("nik", resetPasswordRequest.getNoIdentitas());
        responseData.put("Your OTP code is: ", otpCode);

        // Prepare response data
        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }
}

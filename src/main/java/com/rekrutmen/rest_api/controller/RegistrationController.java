package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.RegisterRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Profile;
import com.rekrutmen.rest_api.model.User;
import com.rekrutmen.rest_api.service.ProfileService;
import com.rekrutmen.rest_api.service.UserService;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<ResponseWrapper<Object>> register(@RequestBody RegisterRequest registerRequest) {
        // Check if username or email is already taken
        if (userService.isUsernameTaken(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "400",
                    responseCodeUtil.getMessage("400"),
                    "Username already exist"
            ));
        }

        if (userService.isEmailTaken(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "400",
                    responseCodeUtil.getMessage("400"),
                    "Email already registered"
            ));
        }

        if (profileService.isNikExist(registerRequest.getNik())) {
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "400",
                    responseCodeUtil.getMessage("400"),
                    "NIK already registered"
            ));
        }

        // Encrypt the password
        String encryptedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // Create and save new user
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(registerRequest.getEmail());
        userService.registerUser(newUser);

        // Create and save new profile
        Profile profile = new Profile();
        //profile.setUserId(newUser.getIdUsers());
        profile.setEmail(registerRequest.getEmail());
        profile.setNik(registerRequest.getNik());
        profile.setCreatedAt(LocalDateTime.now());
        profileService.createProfile(profile);

        userService.registerUser(newUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("username", registerRequest.getUsername());
        responseData.put("nik", registerRequest.getNik());
        responseData.put("email", registerRequest.getEmail());
        responseData.put("password", registerRequest.getPassword());

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }
}

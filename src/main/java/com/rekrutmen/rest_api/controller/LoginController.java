package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.User;
import com.rekrutmen.rest_api.service.LoginService;
import com.rekrutmen.rest_api.service.UserService;
import com.rekrutmen.rest_api.util.JwtUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
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

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<ResponseWrapper<Object>> login(@RequestBody User loginRequest) {
        // Check if user exists by email
        Optional<User> user = userService.getUserByEmail(loginRequest.getEmail());
        if (user.isPresent()) {
            User existingUser = user.get();

            // Verify the password
            if (passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
                // Generate JWT token
                String token = JwtUtil.generateToken(loginRequest.getEmail());

                // Update the token in the database
                existingUser.setToken(token);
                userService.updateUser(existingUser);

                // Prepare response data
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                responseData.put("email", loginRequest.getEmail());

                return ResponseEntity.ok(new ResponseWrapper<>(
                        "000",
                        responseCodeUtil.getMessage("000"),
                        responseData
                ));
            }
        }

        // Return unauthorized response
        return ResponseEntity.status(401).body(new ResponseWrapper<>(
                "401",
                responseCodeUtil.getMessage("401"),
                "Invalid email or password"
        ));
    }
}

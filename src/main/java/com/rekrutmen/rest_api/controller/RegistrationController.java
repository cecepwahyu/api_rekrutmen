package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.RegisterRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.service.AuthService;
import com.rekrutmen.rest_api.service.RegistrationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Request Data = {Name: {}, Username: {}, No Identitas: {}, Email: {}, Password: {}}", registerRequest.getNama(), registerRequest.getNoIdentitas(), registerRequest.getEmail(), registerRequest.getPassword());
        return registrationService.handleRegister(registerRequest);
    }
}

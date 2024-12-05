package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.RegisterRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {


    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return registrationService.handleRegister(registerRequest);
    }
}

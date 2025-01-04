package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.RegisterRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.util.MaskingUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private PesertaService pesertaService;

    @Mock
    private ResponseCodeUtil responseCodeUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleRegister_SuccessfulRegistration() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("new_user");
        registerRequest.setEmail("new_email@bpddiy.com");
        registerRequest.setPassword("password123");
        registerRequest.setNoIdentitas("123456789");

        Peserta newPeserta = new Peserta();
        newPeserta.setUsername("new_user");
        newPeserta.setEmail("new_email@bpddiy.com");
        newPeserta.setNoIdentitas("123456789");

        when(pesertaService.isUsernameTaken("new_user")).thenReturn(false);
        when(pesertaService.isEmailTaken("new_email@bpddiy.com")).thenReturn(false);
        when(pesertaService.isNoIdentitasExist("123456789")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encrypted_password");
        when(responseCodeUtil.getMessage("000")).thenReturn("Success");

        // Act
        ResponseEntity<ResponseWrapper<Object>> response = registrationService.handleRegister(registerRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("000", response.getBody().getResponseCode());
        assertEquals("Success", response.getBody().getResponseMessage());

        Map<String, Object> responseData = (Map<String, Object>) response.getBody().getData();
        assertEquals("new_user", responseData.get("username"));
        assertEquals("123456789", responseData.get("no_identitas"));
        assertEquals("new_email@bpddiy.com", responseData.get("email"));

        verify(pesertaService, times(1)).registerUser(any(Peserta.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void testHandleRegister_WhenUsernameExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("existing_user");
        registerRequest.setEmail("new_email@bpddiy.com");
        registerRequest.setPassword("password123");
        registerRequest.setNoIdentitas("123456789");

        when(pesertaService.isUsernameTaken("existing_user")).thenReturn(true);
        when(responseCodeUtil.getMessage("400")).thenReturn("Bad Request");

        // Act
        ResponseEntity<ResponseWrapper<Object>> response = registrationService.handleRegister(registerRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("400", response.getBody().getResponseCode());
        assertEquals("Username already exist", response.getBody().getData());

        verify(pesertaService, times(1)).isUsernameTaken("existing_user");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void testHandleRegister_WhenEmailExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("new_user");
        registerRequest.setEmail("existing_email@bpddiy.com");
        registerRequest.setPassword("password123");
        registerRequest.setNoIdentitas("123456789");

        when(pesertaService.isUsernameTaken("new_user")).thenReturn(false);
        when(pesertaService.isEmailTaken("existing_email@bpddiy.com")).thenReturn(true);
        when(responseCodeUtil.getMessage("400")).thenReturn("Bad Request");

        // Act
        ResponseEntity<ResponseWrapper<Object>> response = registrationService.handleRegister(registerRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("400", response.getBody().getResponseCode());
        assertEquals("Email already registered", response.getBody().getData());

        verify(pesertaService, times(1)).isUsernameTaken("new_user");
        verify(pesertaService, times(1)).isEmailTaken("existing_email@bpddiy.com");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void testHandleRegister_WhenNoIdentitasExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("new_user");
        registerRequest.setEmail("new_email@bpddiy.com");
        registerRequest.setPassword("password123");
        registerRequest.setNoIdentitas("existing_no_identitas");

        when(pesertaService.isUsernameTaken("new_user")).thenReturn(false);
        when(pesertaService.isEmailTaken("new_email@bpddiy.com")).thenReturn(false);
        when(pesertaService.isNoIdentitasExist("existing_no_identitas")).thenReturn(true);
        when(responseCodeUtil.getMessage("400")).thenReturn("Bad Request");

        // Act
        ResponseEntity<ResponseWrapper<Object>> response = registrationService.handleRegister(registerRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("400", response.getBody().getResponseCode());
        assertEquals("NIK already registered", response.getBody().getData());

        verify(pesertaService, times(1)).isUsernameTaken("new_user");
        verify(pesertaService, times(1)).isEmailTaken("new_email@bpddiy.com");
        verify(pesertaService, times(1)).isNoIdentitasExist("existing_no_identitas");
        verifyNoInteractions(passwordEncoder);
    }
}

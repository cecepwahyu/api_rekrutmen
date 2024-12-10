package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.OtpVerificationRequest;
import com.rekrutmen.rest_api.dto.ResetPasswordRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private ProfileService profileService;

    @Mock
    private EmailService emailService;

    @Mock
    private ResponseCodeUtil responseCodeUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleResetPassword_WithValidData() {
        // Arrange
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmail("test@example.com");
        resetPasswordRequest.setNoIdentitas("123456789");

        Peserta mockPeserta = new Peserta();
        mockPeserta.setIdPeserta(1);
        mockPeserta.setEmail("test@example.com");
        mockPeserta.setNoIdentitas("123456789");

        when(profileService.validateEmailAndNoIdentitas("test@example.com", "123456789"))
                .thenReturn(Optional.of(mockPeserta));
        doNothing().when(profileService).updateOtp(eq(1), anyString(), LocalDateTime.parse("2024-12-10 08:22:47.463392"));
        doNothing().when(emailService).sendOtpEmail(eq("test@example.com"), anyString());
        when(responseCodeUtil.getMessage("000")).thenReturn("Success");

        // Act
        ResponseEntity<ResponseWrapper<Object>> response = authService.handleResetPassword(resetPasswordRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("000", response.getBody().getResponseCode());
        assertEquals("Success", response.getBody().getResponseMessage());
        assertEquals("test@example.com", ((HashMap<?, ?>) response.getBody().getData()).get("email"));

        verify(profileService, times(1)).validateEmailAndNoIdentitas("test@example.com", "123456789");
        verify(profileService, times(1)).updateOtp(eq(1), anyString(), LocalDateTime.parse("2024-12-10 08:22:47.463392"));
        verify(emailService, times(1)).sendOtpEmail(eq("test@example.com"), anyString());
    }

    @Test
    void testHandleResetPassword_WithInvalidData() {
        // Arrange
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmail("invalid@example.com");
        resetPasswordRequest.setNoIdentitas("987654321");

        when(profileService.validateEmailAndNoIdentitas("invalid@example.com", "987654321"))
                .thenReturn(Optional.empty());
        when(responseCodeUtil.getMessage("400")).thenReturn("Invalid email or No Identitas");

        // Act
        ResponseEntity<ResponseWrapper<Object>> response = authService.handleResetPassword(resetPasswordRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("400", response.getBody().getResponseCode());
        assertEquals("Invalid email or No Identitas", response.getBody().getResponseMessage());

        verify(profileService, times(1)).validateEmailAndNoIdentitas("invalid@example.com", "987654321");
        verifyNoInteractions(emailService);
    }

    @Test
    void testHandleOtpVerification_WithValidOtp() {
        // Arrange
        OtpVerificationRequest otpRequest = new OtpVerificationRequest();
        otpRequest.setOtp("123456");

        Peserta mockPeserta = new Peserta();
        mockPeserta.setIdPeserta(1);
        mockPeserta.setOtp("123456");

        when(profileService.validateOtp("123456")).thenReturn(Optional.of(mockPeserta));
        when(responseCodeUtil.getMessage("000")).thenReturn("Success");

        // Act
        ResponseEntity<ResponseWrapper<Object>> response = authService.handleOtpVerification(otpRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("000", response.getBody().getResponseCode());
        assertEquals("Success", response.getBody().getResponseMessage());
        assertEquals("123456", ((HashMap<?, ?>) response.getBody().getData()).get("OTP is valid!"));

        verify(profileService, times(1)).validateOtp("123456");
    }

    @Test
    void testHandleOtpVerification_WithInvalidOtp() {
        // Arrange
        OtpVerificationRequest otpRequest = new OtpVerificationRequest();
        otpRequest.setOtp("654321");

        when(profileService.validateOtp("654321")).thenReturn(Optional.empty());
        when(responseCodeUtil.getMessage("400")).thenReturn("Invalid OTP code");

        // Act
        ResponseEntity<ResponseWrapper<Object>> response = authService.handleOtpVerification(otpRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("400", response.getBody().getResponseCode());
        assertEquals("Invalid OTP code", response.getBody().getResponseMessage());

        verify(profileService, times(1)).validateOtp("654321");
    }
}

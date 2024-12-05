package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.PengumumanUmum;
import com.rekrutmen.rest_api.repository.PengumumanUmumRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class PengumumanUmumServiceTest {

    @Mock
    private PengumumanUmumRepository pengumumanUmumRepository;

    @InjectMocks
    private PengumumanUmumService pengumumanUmumService;

    public PengumumanUmumServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPengumumanUmums() {
        //Arrange
        PengumumanUmum pengumumanUmum1 = new PengumumanUmum();
        pengumumanUmum1.setId(UUID.fromString("b3cc87b2-d6c8-4024-ad4a-0d4aea29fd2a"));
        pengumumanUmum1.setJudul("Pembukaan Management Trainee");
        pengumumanUmum1.setSlug("pembukaan-management-trainee");
        pengumumanUmum1.setIsi("BPD DIY membuka lowongan management trainee");
        pengumumanUmum1.setStatus(true);
        pengumumanUmum1.setCreatedAt(LocalDateTime.now());
        pengumumanUmum1.setUpdatedAt(LocalDateTime.now());
        pengumumanUmum1.setUpdatedBy(1);
        pengumumanUmum1.setCreatedBy(1);

        PengumumanUmum pengumumanUmum2 = new PengumumanUmum();
        pengumumanUmum2.setId(UUID.fromString("b4cc87b2-d6c8-4024-ad4a-0d4aea29fd3b"));
        pengumumanUmum2.setJudul("Pembukaan IT Trainee");
        pengumumanUmum2.setSlug("pembukaan-it-trainee");
        pengumumanUmum2.setIsi("BPD DIY membuka lowongan IT Trainee");
        pengumumanUmum2.setStatus(false);
        pengumumanUmum2.setCreatedAt(LocalDateTime.now());
        pengumumanUmum2.setUpdatedAt(LocalDateTime.now());
        pengumumanUmum2.setUpdatedBy(2);
        pengumumanUmum2.setCreatedBy(2);

        when(pengumumanUmumRepository.findAll()).thenReturn(Arrays.asList(pengumumanUmum1, pengumumanUmum2));

        // Act
        List<PengumumanUmum> result = pengumumanUmumRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Pembukaan Management Trainee", result.get(0).getJudul());
        assertEquals("Pembukaan IT Trainee", result.get(1).getJudul());
        assertEquals("BPD DIY membuka lowongan management trainee", result.get(0).getIsi());
        assertEquals("BPD DIY membuka lowongan IT Trainee", result.get(1).getIsi());
        assertEquals("pembukaan-management-trainee", result.get(0).getSlug());
        assertEquals("pembukaan-it-trainee", result.get(1).getSlug());
    }

    @Test
    void testGetAllPengumumanUmums() {
        // Arrange
        when(pengumumanUmumRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<PengumumanUmum> result = pengumumanUmumRepository.findAll();

        // Assert
        assertNotNull(result, "Result should not be null.");
        assertTrue(result.isEmpty(), "Result should be an empty list.");
    }

    @Test
    void testGetAllPengumumanUmumsThrowsException() {
        // Arrange
        RuntimeException repositoryException = new RuntimeException("Database connection error");
        doThrow(repositoryException).when(pengumumanUmumRepository).findAll();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> pengumumanUmumRepository.findAll());
        assertEquals("Database connection error", exception.getMessage(), "Exception message should match the repository exception.");
    }
}
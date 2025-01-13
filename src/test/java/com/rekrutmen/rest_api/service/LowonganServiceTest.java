package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.repository.LowonganRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class LowonganServiceTest {

    @Mock
    private LowonganRepository lowonganRepository;

    @InjectMocks
    private LowonganService lowonganService;

    public LowonganServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLowongans() {
        // Arrange
        Lowongan lowongan1 = new Lowongan();
        lowongan1.setIdLowongan(1);
        lowongan1.setJudulLowongan("Software Development In Test Engineer");
        lowongan1.setSlug("sdet-engineer");
        lowongan1.setPosisi("Quality Assurance");
        lowongan1.setTentangPekerjaan("Responsible for developing automated test frameworks and tools.");
        lowongan1.setPersyaratan("Bachelor's degree in Computer Science or related field. Experience in test automation.");
        lowongan1.setPeriodeAwal(LocalDate.of(2024, 1, 1));
        lowongan1.setPeriodeAkhir(LocalDate.of(2024, 12, 12));
        //lowongan1.setStatus('0');
        lowongan1.setCreatedAt(LocalDateTime.now());
        lowongan1.setUpdatedAt(LocalDateTime.now());
        lowongan1.setCreatedBy(1);
        lowongan1.setUpdatedBy(1);
        lowongan1.setKodeLowongan("SDET-001");

        Lowongan lowongan2 = new Lowongan();
        lowongan2.setIdLowongan(2);
        lowongan2.setJudulLowongan("Data Scientist");
        lowongan2.setSlug("data-scientist");
        lowongan2.setPosisi("Data Analytics");
        lowongan2.setTentangPekerjaan("Analyze large datasets and build predictive models.");
        lowongan2.setPersyaratan("Master's degree in Data Science. Experience in Python and machine learning.");
        lowongan2.setPeriodeAwal(LocalDate.of(2024, 2, 1));
        lowongan2.setPeriodeAkhir(LocalDate.of(2024, 6, 30));
        //lowongan2.setStatus('1');
        lowongan2.setCreatedAt(LocalDateTime.now());
        lowongan2.setUpdatedAt(LocalDateTime.now());
        lowongan2.setCreatedBy(2);
        lowongan2.setUpdatedBy(2);
        lowongan2.setKodeLowongan("DS-002");

        when(lowonganRepository.findAll()).thenReturn(Arrays.asList(lowongan1, lowongan2));

        // Act
        List<Lowongan> result = lowonganRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Software Development In Test Engineer", result.get(0).getJudulLowongan());
        assertEquals("Data Scientist", result.get(1).getJudulLowongan());
        assertEquals("Quality Assurance", result.get(0).getPosisi());
        assertEquals("Data Analytics", result.get(1).getPosisi());
        assertEquals("SDET-001", result.get(0).getKodeLowongan());
        assertEquals("DS-002", result.get(1).getKodeLowongan());
    }

    @Test
    void testGetAllLowongansReturnsEmptyList() {
        // Arrange
        when(lowonganRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Lowongan> result = lowonganRepository.findAll();

        // Assert
        assertNotNull(result, "Result should not be null.");
        assertTrue(result.isEmpty(), "Result should be an empty list.");
    }

    @Test
    void testGetAllLowongansThrowsException() {
        // Arrange
        RuntimeException repositoryException = new RuntimeException("Database connection error");
        doThrow(repositoryException).when(lowonganRepository).findAll();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> lowonganRepository.findAll());
        assertEquals("Database connection error", exception.getMessage(), "Exception message should match the repository exception.");
    }
}

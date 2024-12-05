package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.repository.ArtikelRepository;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ArtikelServiceTest {

    @InjectMocks
    private ArtikelService artikelService;

    @Mock
    private ArtikelRepository artikelRepository;

    @Mock
    private TokenUtil tokenUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetArtikelList_WithValidToken() {
        // Arrange
        String validToken = "valid_token";
        when(tokenUtil.isValidToken(validToken)).thenReturn(true);

        Artikel artikel1 = createArtikel("Judul 1", "slug-1", "gambar1.jpg", "Isi artikel 1", 1, 1);
        Artikel artikel2 = createArtikel("Judul 2", "slug-2", "gambar2.jpg", "Isi artikel 2", 2, 2);

        List<Artikel> mockArtikels = Arrays.asList(artikel1, artikel2);
        when(artikelRepository.findAll()).thenReturn(mockArtikels);

        // Act
        ResponseEntity<ResponseWrapper<List<Artikel>>> response = artikelService.getArtikelList(validToken);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("000", response.getBody().getResponseCode());
        assertEquals("Success", response.getBody().getResponseMessage());
        assertEquals(2, response.getBody().getData().size());

        verify(tokenUtil, times(1)).isValidToken(validToken);
        verify(artikelRepository, times(1)).findAll();
    }

    @Test
    void testGetArtikelList_WithInvalidToken() {
        // Arrange
        String invalidToken = "invalid_token";
        when(tokenUtil.isValidToken(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<ResponseWrapper<List<Artikel>>> response = artikelService.getArtikelList(invalidToken);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("401", response.getBody().getResponseCode());
        assertEquals("Unauthorized", response.getBody().getResponseMessage());
        assertEquals(null, response.getBody().getData());

        verify(tokenUtil, times(1)).isValidToken(invalidToken);
        verifyNoInteractions(artikelRepository);
    }

    // Helper method to create mock Artikel objects
    private Artikel createArtikel(String judul, String slug, String gambar, String isi, Integer createdBy, Integer updatedBy) {
        Artikel artikel = new Artikel();
        artikel.setId(UUID.randomUUID());
        artikel.setJudul(judul);
        artikel.setSlug(slug);
        artikel.setGambar(gambar);
        artikel.setIsi(isi);
        artikel.setStatus(true);
        artikel.setCreatedAt(LocalDateTime.now());
        artikel.setUpdatedAt(LocalDateTime.now());
        artikel.setCreatedBy(createdBy);
        artikel.setUpdatedBy(updatedBy);
        return artikel;
    }
}

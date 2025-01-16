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

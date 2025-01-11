package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.model.Lowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtikelRepository extends JpaRepository<Artikel, UUID> {
    Optional<Artikel> findById(UUID id);
    Optional<Artikel> findBySlug(String slug);
    Optional<Artikel> findByGambar(String gambar);
}
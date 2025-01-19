package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.model.PengumumanUmum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtikelRepository extends JpaRepository<Artikel, Long> {
    Optional<Artikel> findById(UUID id);
    Optional<Artikel> findBySlug(String slug);
    Optional<Artikel> findByGambar(String gambar);

    @Query("SELECT p FROM Artikel p WHERE p.statusPublish = '1' AND p.approved IS NOT NULL AND p.approved = true ORDER BY p.createdAt DESC")
    Page<Artikel> findPublishedAndApproved(Pageable pageable);
}
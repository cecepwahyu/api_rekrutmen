package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PengumumanUmum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PengumumanUmumRepository extends JpaRepository<PengumumanUmum, UUID> {
    Optional<PengumumanUmum> findById(UUID id);
    Optional<PengumumanUmum> findBySlug(String slug);

    @Query("SELECT p FROM PengumumanUmum p WHERE p.statusPublish = '1' AND p.approved IS NOT NULL AND p.approved = true ORDER BY p.createdAt DESC")
    Page<PengumumanUmum> findPublishedAndApproved(Pageable pageable);

}
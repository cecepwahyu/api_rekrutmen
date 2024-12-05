package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.model.PengumumanUmum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PengumumanUmumRepository extends JpaRepository<PengumumanUmum, UUID> {
    Optional<PengumumanUmum> findById(UUID id);
}
package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Lowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LowonganRepository extends JpaRepository<Lowongan, Integer> {
    Optional<Lowongan> findByIdLowongan(Long idLowongan);
    Optional<Lowongan> findBySlug(String slug);
}
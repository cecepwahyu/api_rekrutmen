package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Artikel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtikelRepository extends JpaRepository<Artikel, UUID> {
}
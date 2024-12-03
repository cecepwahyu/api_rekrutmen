package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.model.Lowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LowonganRepository extends JpaRepository<Lowongan, Integer> {
}
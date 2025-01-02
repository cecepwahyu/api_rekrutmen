package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaLowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesertaLowonganRepository extends JpaRepository<PesertaLowongan, String> {
}


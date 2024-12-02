package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaPengalaman;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesertaPengalamanRepository extends JpaRepository<PesertaPengalaman, Integer> {
    void deleteByIdPeserta(Integer idPeserta);
}
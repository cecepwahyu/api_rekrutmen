package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaPengalaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PesertaPengalamanRepository extends JpaRepository<PesertaPengalaman, Integer> {
    Optional<PesertaPengalaman> findByIdPeserta(Integer idPeserta);
    void deleteByIdPeserta(Integer idPeserta);
}
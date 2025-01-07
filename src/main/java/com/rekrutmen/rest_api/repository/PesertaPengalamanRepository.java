package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaPengalaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PesertaPengalamanRepository extends JpaRepository<PesertaPengalaman, Integer> {
    List<PesertaPengalaman> findByIdPeserta(Integer idPeserta);
    void deleteByIdPeserta(Integer idPeserta);
}
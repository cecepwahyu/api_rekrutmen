package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaOrganisasi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PesertaOrganisasiRepository extends JpaRepository<PesertaOrganisasi, Integer> {
    Optional<PesertaOrganisasi> findByIdPeserta(Integer idPeserta);
    void deleteByIdPeserta(Integer idPeserta);
}
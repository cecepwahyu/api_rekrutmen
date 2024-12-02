package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaOrganisasi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesertaOrganisasiRepository extends JpaRepository<PesertaOrganisasi, Integer> {
    void deleteByIdPeserta(Integer idPeserta);
}
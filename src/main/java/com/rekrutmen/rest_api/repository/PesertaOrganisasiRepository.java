package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaOrganisasi;
import com.rekrutmen.rest_api.model.PesertaPendidikan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PesertaOrganisasiRepository extends JpaRepository<PesertaOrganisasi, Integer> {
    List<PesertaOrganisasi> findByIdPeserta(Integer idPeserta);
    void deleteByIdPeserta(Integer idPeserta);
}
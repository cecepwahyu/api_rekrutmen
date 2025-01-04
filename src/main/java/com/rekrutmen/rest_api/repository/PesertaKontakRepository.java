package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaKontak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PesertaKontakRepository extends JpaRepository<PesertaKontak, Integer> {
    Optional<PesertaKontak> findByIdPeserta(Integer idPeserta);
    void deleteByIdPeserta(Integer idPesera);
}

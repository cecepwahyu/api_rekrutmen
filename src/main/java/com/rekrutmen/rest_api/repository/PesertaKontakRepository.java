package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaKontak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesertaKontakRepository extends JpaRepository<PesertaKontak, Integer> {
    void deleteByIdPeserta(Integer idPesera);
}

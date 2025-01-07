package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaKontak;
import com.rekrutmen.rest_api.model.PesertaPendidikan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PesertaKontakRepository extends JpaRepository<PesertaKontak, Integer> {
    List<PesertaKontak> findByIdPeserta(Integer idPeserta);
    void deleteByIdPeserta(Integer idPesera);
}

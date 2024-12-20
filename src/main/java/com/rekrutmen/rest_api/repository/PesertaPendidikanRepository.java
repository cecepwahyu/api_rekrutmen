package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaPendidikan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PesertaPendidikanRepository extends JpaRepository<PesertaPendidikan, Integer> {
    Optional<PesertaPendidikan> findByIdPeserta(Integer idPeserta);
    void deleteByIdPeserta(Integer idPeserta); // Use the correct field name
}

package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaPendidikan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesertaPendidikanRepository extends JpaRepository<PesertaPendidikan, Integer> {
    void deleteByIdPeserta(Integer idPeserta); // Use the correct field name
}

package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.LogPesertaLowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogPesertaLowonganRepository extends JpaRepository<LogPesertaLowongan, Long> {
}

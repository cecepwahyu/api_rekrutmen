package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.VwPesertaLowonganHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VwPesertaLowonganHistoryRepository extends JpaRepository<VwPesertaLowonganHistory, Long> {

    Optional<VwPesertaLowonganHistory> findByIdPeserta(Long idPeserta);

    Optional<VwPesertaLowonganHistory> findBySlug(String slug);

    List<VwPesertaLowonganHistory> findAllByTahunAplikasi(Integer tahunAplikasi);
}

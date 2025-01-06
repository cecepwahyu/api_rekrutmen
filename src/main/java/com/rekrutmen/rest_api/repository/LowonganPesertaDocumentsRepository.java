package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.LowonganPesertaDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LowonganPesertaDocumentsRepository extends JpaRepository<LowonganPesertaDocuments, Integer> {
}
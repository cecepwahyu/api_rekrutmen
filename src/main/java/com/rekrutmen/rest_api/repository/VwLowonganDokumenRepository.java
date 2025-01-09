package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.VwLowonganDokumen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwLowonganDokumenRepository extends JpaRepository<VwLowonganDokumen, Integer> {

    @Query(value = "SELECT * FROM vw_lowongan_dokumen WHERE slug = :slug ORDER BY sort_order", nativeQuery = true)
    List<Object[]> findAllBySlug(@Param("slug") String slug);
}
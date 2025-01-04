package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.TahapanSeleksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TahapanSeleksiRepository extends JpaRepository<TahapanSeleksi, Integer> {
    Optional<TahapanSeleksi> findByIdTahapan(Integer idTahapan);

    @Query("SELECT t FROM TahapanSeleksi t ORDER BY t.idTahapan ASC")
    List<TahapanSeleksi> findAllOrderedByIdTahapan();

    @Query(value = "SELECT * FROM vw_lowongan_tahapan WHERE lowongan_id = :lowonganId ORDER BY sort_order", nativeQuery = true)
    List<Object[]> findAllTahapanByLowonganId(@Param("lowonganId") Integer lowonganId);

    @Query(value = "SELECT * FROM vw_lowongan_tahapan WHERE slug = :slug ORDER BY sort_order", nativeQuery = true)
    List<Object[]> findAllTahapanBySlug(@Param("slug") String slug);
}
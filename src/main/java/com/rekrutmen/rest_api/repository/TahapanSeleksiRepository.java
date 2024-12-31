package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.TahapanSeleksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TahapanSeleksiRepository extends JpaRepository<TahapanSeleksi, Integer> {
    Optional<TahapanSeleksi> findByIdTahapan(Integer idTahapan);
}
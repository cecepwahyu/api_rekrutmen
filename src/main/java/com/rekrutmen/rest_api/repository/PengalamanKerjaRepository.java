package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PengalamanKerja;
import com.rekrutmen.rest_api.model.RiwayatPendidikan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PengalamanKerjaRepository extends JpaRepository<PengalamanKerja, Integer> {
    void deleteByProfileId(Integer profileId);
}
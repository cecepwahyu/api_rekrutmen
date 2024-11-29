package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.RiwayatOrganisasi;
import com.rekrutmen.rest_api.model.RiwayatPendidikan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiwayatOrganisasiRepository extends JpaRepository<RiwayatOrganisasi, Integer> {
    void deleteByProfileId(Integer profileId);
}
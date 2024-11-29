package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.RiwayatPendidikan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiwayatPendidikanRepository extends JpaRepository<RiwayatPendidikan, Integer> {
    void deleteByProfileId(Integer profileId);
}
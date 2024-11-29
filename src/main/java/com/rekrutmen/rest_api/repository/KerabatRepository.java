package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Kerabat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KerabatRepository extends JpaRepository<Kerabat, Integer> {
    void deleteByProfileId(Integer profileId);
}

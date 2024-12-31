package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.ProgresTahapan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgresTahapanRepository extends JpaRepository<ProgresTahapan, Integer> {
    Optional<ProgresTahapan> findById(Integer id);
}
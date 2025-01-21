package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Kabupaten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KabupatenRepository extends JpaRepository<Kabupaten, String> {
    List<Kabupaten> findByKodeProvinsi(String kodeProvinsi);
}

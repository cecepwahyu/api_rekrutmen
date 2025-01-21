package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Kecamatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KecamatanRepository extends JpaRepository<Kecamatan, String> {
    List<Kecamatan> findByKodeKabupaten(String kodeKabupaten);
}

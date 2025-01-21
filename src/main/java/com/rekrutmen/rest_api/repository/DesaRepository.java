package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Desa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesaRepository extends JpaRepository<Desa, String> {
    List<Desa> findByKodeKecamatan(String kodeKecamatan);
}

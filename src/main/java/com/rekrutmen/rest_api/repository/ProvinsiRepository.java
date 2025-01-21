package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Provinsi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinsiRepository extends JpaRepository<Provinsi, String> {
}

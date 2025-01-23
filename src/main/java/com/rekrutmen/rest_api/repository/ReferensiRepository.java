package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Referensi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReferensiRepository extends JpaRepository<Referensi, Integer> {

    @Query("SELECT r FROM Referensi r WHERE r.refGroup1 = :refGroup1")
    List<Referensi> findByGroup1(@Param("refGroup1") String refGroup1);
}

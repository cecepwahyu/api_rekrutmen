package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Lowongan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LowonganRepository extends JpaRepository<Lowongan, Integer> {
    Optional<Lowongan> findByIdLowongan(Long idLowongan);
    Optional<Lowongan> findBySlug(String slug);

    // Custom query with pagination
    @Query("SELECT l FROM Lowongan l WHERE l.status IN ('1', '4') AND l.flgApprove = true")
    Page<Lowongan> findByStatusAndFlgApprove(Pageable pageable);
}
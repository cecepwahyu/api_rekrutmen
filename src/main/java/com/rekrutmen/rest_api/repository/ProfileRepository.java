package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    boolean existsByNik(String nik);
    Optional<Profile> findByEmailAndNik(String email, String nik);
}



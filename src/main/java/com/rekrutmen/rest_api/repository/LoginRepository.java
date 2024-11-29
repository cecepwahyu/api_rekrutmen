package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Login;
import com.rekrutmen.rest_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, String> {
    Optional<User> findByEmail(String email);
}

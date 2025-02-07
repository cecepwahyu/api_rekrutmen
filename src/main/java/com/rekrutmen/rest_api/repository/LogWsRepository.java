package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.LogWs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogWsRepository extends JpaRepository<LogWs, Long> {
}

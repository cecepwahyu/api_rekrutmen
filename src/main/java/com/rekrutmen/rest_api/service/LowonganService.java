package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.repository.LowonganRepository;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LowonganService {

    @Autowired
    private LowonganRepository lowonganRepository;

    @Autowired
    private TokenUtil tokenUtil;

    public ResponseEntity<ResponseWrapper<List<Lowongan>>> getLowonganList(String token) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    "401",
                    "Unauthorized",
                    null
            ));
        }

        // Fetch lowongan list
        List<Lowongan> lowongans = lowonganRepository.findAll();
        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Success",
                lowongans
        ));
    }
}

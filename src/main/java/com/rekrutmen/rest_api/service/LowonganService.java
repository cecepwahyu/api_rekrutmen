package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.repository.LowonganRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LowonganService {

    @Autowired
    private LowonganRepository lowonganRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<List<Lowongan>>> getLowonganList(String token) {

        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    null
            ));
        }

        // Fetch lowongan list
        List<Lowongan> lowongans = lowonganRepository.findAll();
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                lowongans
        ));
    }

    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongans(String token, Integer page) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    null
            ));
        }

        // Handle null or negative page numbers gracefully
        if (page == null || page < 0) {
            page = 0; // Default to the first page
        }

        // Create a pageable object with the desired page and size
        Pageable pageable = PageRequest.of(page, 6); // 6 articles per page

        // Fetch articles with pagination
        Page<Lowongan> lowongansPage = lowonganRepository.findAll(pageable);

        // Create a response wrapper with pagination info
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("content", lowongansPage.getContent());
        responseData.put("currentPage", lowongansPage.getNumber());
        responseData.put("totalItems", lowongansPage.getTotalElements());
        responseData.put("totalPages", lowongansPage.getTotalPages());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    public ResponseEntity<ResponseWrapper<Lowongan>> getLowonganDetail(String token, Long idLowongan) {

        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    null
            ));
        }

        // Fetch lowongan details
        Lowongan lowongan = lowonganRepository.findByIdLowongan(idLowongan).orElse(null);

        if (lowongan == null) {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                lowongan
        ));
    }

}

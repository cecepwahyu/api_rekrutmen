package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.repository.LowonganRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // Lowongan Status 1 & 4
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

        // Create a pageable object with the desired page, size, and sorting by idLowongan descending
        Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "idLowongan"));

        // Fetch articles with pagination and apply custom query to filter by status and flg_approve
        Page<Lowongan> lowongansPage = lowonganRepository.findByStatusAndFlgApprove(pageable);

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

    //GET REKRUTMEN LIST
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongansRekrutmen(String token, Integer page) {
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

        // Create a pageable object with the desired page, size, and sorting by idLowongan descending
        Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "idLowongan"));

        // Fetch articles with pagination and apply custom query to filter by status and flg_approve
        Page<Lowongan> lowongansPage = lowonganRepository.findApprovedLowonganRekrutmen(pageable);

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

    // GET JOB DESC
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongansJobDesc(String token, Integer page) {
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

        // Create a pageable object with the desired page, size, and sorting by idLowongan descending
        Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "idLowongan"));

        // Fetch articles with pagination and apply custom query to filter by status and flg_approve
        Page<Lowongan> lowongansPage = lowonganRepository.findApprovedLowonganJobDesc(pageable);

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

    //Get lowongan by Slug
    public ResponseEntity<ResponseWrapper<Lowongan>> getLowonganDetailSlug(String token, String slug) {
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

        // Fetch article details by ID
        Lowongan lowongan = lowonganRepository.findBySlug(slug).orElse(null);

        if (lowongan == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
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

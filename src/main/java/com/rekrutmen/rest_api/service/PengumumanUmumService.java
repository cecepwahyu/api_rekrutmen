package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PengumumanUmum;
import com.rekrutmen.rest_api.repository.PengumumanUmumRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PengumumanUmumService {

    private static final Logger logger = LoggerFactory.getLogger(PengumumanUmumService.class);

    @Autowired
    private PengumumanUmumRepository pengumumanUmumRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<Object>> getPaginatedPengumumanUmums(String token, Integer page) {
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

        // Create a pageable object with the desired page and size, 6 articles per page
        Pageable pageable = PageRequest.of(page, 6);

        // Fetch articles with pagination using the new repository method
        Page<PengumumanUmum> pengumumanUmumsPage = pengumumanUmumRepository.findPublishedAndApproved(pageable);

        // Create a response wrapper with pagination info
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("content", pengumumanUmumsPage.getContent());
        responseData.put("currentPage", pengumumanUmumsPage.getNumber());
        responseData.put("totalItems", pengumumanUmumsPage.getTotalElements());
        responseData.put("totalPages", pengumumanUmumsPage.getTotalPages());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    //Get artikel by Slug
    public ResponseEntity<ResponseWrapper<PengumumanUmum>> getPengumumanUmumDetailSlug(String token, String slug) {
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

        // Fetch PengumumanUmum details by Slug
        PengumumanUmum pengumumanUmum = pengumumanUmumRepository.findBySlug(slug).orElse(null);

        if (pengumumanUmum == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pengumumanUmum
        ));
    }
}

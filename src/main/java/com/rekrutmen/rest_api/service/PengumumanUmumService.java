package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PengumumanUmum;
import com.rekrutmen.rest_api.repository.PengumumanUmumRepository;
import com.rekrutmen.rest_api.util.LoggerUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    public ResponseEntity<ResponseWrapper<Object>> getPaginatedPengumumanUmums(String processName, String token, Integer page, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                Map.of("token", token)
        ));

        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    Map.of("message", "Invalid token")
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    Map.of("message", "Expired token")
            ));
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

        if (pengumumanUmumsPage.isEmpty()) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    Map.of("message", "Article not found")
            ));
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        // Create a response wrapper with pagination info
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("content", pengumumanUmumsPage.getContent());
        responseData.put("currentPage", pengumumanUmumsPage.getNumber());
        responseData.put("totalItems", pengumumanUmumsPage.getTotalElements());
        responseData.put("totalPages", pengumumanUmumsPage.getTotalPages());

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        responseCodeUtil.getMessage("000"),
                        responseData
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    //Get artikel by Slug
    public ResponseEntity<ResponseWrapper<PengumumanUmum>> getPengumumanUmumDetailSlug(String processName, String token, String slug, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                Map.of("token", token)
        ));

        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    Map.of("message", "Invalid token")
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    Map.of("message", "Expired token")
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    null
            ));
        }

        // Fetch PengumumanUmum details by Slug
        PengumumanUmum pengumumanUmum = pengumumanUmumRepository.findBySlug(slug).orElse(null);

        if (pengumumanUmum == null) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    Map.of("message", "Pengumuman not found")
            ));
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        responseCodeUtil.getMessage("000"),
                        pengumumanUmum
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pengumumanUmum
        ));
    }
}

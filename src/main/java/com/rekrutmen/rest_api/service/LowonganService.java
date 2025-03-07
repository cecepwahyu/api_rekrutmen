package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.repository.LowonganRepository;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LowonganService {

    private static final Logger logger = LoggerFactory.getLogger(LowonganService.class);

    @Autowired
    private LowonganRepository lowonganRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<List<Lowongan>>> getAllLowongans(String processName, String token, HttpServletRequest request) {
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

        // Fetch all approved lowongans
        List<Lowongan> lowongans = lowonganRepository.findAllApprovedLowongan();

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        responseCodeUtil.getMessage("000"),
                        lowongans
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                lowongans
        ));
    }

    public ResponseEntity<ResponseWrapper<List<Lowongan>>> getAllJobDesc(String processName, String token, HttpServletRequest request) {
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

        // Fetch all approved lowongans
        List<Lowongan> lowongans = lowonganRepository.findAllApprovedJobDesc();

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        responseCodeUtil.getMessage("000"),
                        lowongans
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                lowongans
        ));
    }

    //GET REKRUTMEN LIST
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongansRekrutmen(String processName, String token, Integer page, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                Map.of("token", token, "page", page)
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

    // GET JOB DESC
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongansJobDesc(String processName, String token, Integer page, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                Map.of("token", token, "page", page)
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

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    //Get lowongan by Slug
    public ResponseEntity<ResponseWrapper<Lowongan>> getLowonganDetailSlug(String processName, String token, String slug, HttpServletRequest request) {
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

        // Fetch article details by ID
        Lowongan lowongan = lowonganRepository.findBySlug(slug).orElse(null);

        if (lowongan == null) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    Map.of("message", "Lowongan not found")
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
                        lowongan
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                lowongan
        ));
    }
}

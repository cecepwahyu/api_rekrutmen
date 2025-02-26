package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.repository.TahapanSeleksiRepository;
import com.rekrutmen.rest_api.util.LoggerUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TahapanSeleksiService {

    private static final Logger logger = LoggerFactory.getLogger(TahapanSeleksiService.class);

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private TahapanSeleksiRepository tahapanSeleksiRepository;

    /**
     * Fetches tahapan for a given lowongan ID.
     *
     * @param token      the authorization token
     * @param lowonganId the ID of the lowongan
     * @return ResponseEntity with the list of tahapan or an appropriate error message
     */
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanByLowonganId(String processName, String token, Integer lowonganId, HttpServletRequest request) {
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

        try {
            // Fetch tahapan from the repository
            List<Object[]> tahapanList = tahapanSeleksiRepository.findAllTahapanByLowonganId(lowonganId);

            if (tahapanList == null || tahapanList.isEmpty()) {
                logger.warn(LoggerUtil.formatLog(
                        jobId, ip, "R", processName,
                        responseCodeUtil.getCode("077"),
                        responseCodeUtil.getMessage("077"),
                        Map.of("message", "Artikel not found")
                ));
                return ResponseEntity.status(400).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("077"),
                        responseCodeUtil.getCode("077"),
                        null
                ));
            }

            logger.info(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    new ResponseWrapper<>(
                            responseCodeUtil.getCode("000"),
                            responseCodeUtil.getMessage("000"),
                            tahapanList
                    )
            ));
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    tahapanList
            ));

        } catch (Exception e) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    Map.of("message", "Internal server error while fetching tahapan")
            ));
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Internal server error while fetching tahapan",
                    null
            ));
        }
    }


    /**
     * Fetches tahapan for a given SLUG.
     *
     * @param token      the authorization token
     * @param slug the slug of the lowongan
     * @return ResponseEntity with the list of tahapan or an appropriate error message
     */
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanBySlug(String processName, String token, String slug, HttpServletRequest request) {
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

        try {
            // Fetch tahapan from the repository
            List<Object[]> tahapanList = tahapanSeleksiRepository.findAllTahapanBySlug(slug);

            if (tahapanList == null || tahapanList.isEmpty()) {
                logger.warn(LoggerUtil.formatLog(
                        jobId, ip, "R", processName,
                        responseCodeUtil.getCode("077"),
                        responseCodeUtil.getMessage("077"),
                        Map.of("message", "Artikel not found")
                ));
                return ResponseEntity.status(400).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        "No tahapan found for Slug: " + slug,
                        null
                ));
            }

            logger.info(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    new ResponseWrapper<>(
                            responseCodeUtil.getCode("000"),
                            responseCodeUtil.getMessage("000"),
                            tahapanList
                    )
            ));
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    tahapanList
            ));

        } catch (Exception e) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    Map.of("message", "Internal server error while fetching tahapan")
            ));
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Internal server error while fetching tahapan",
                    null
            ));
        }
    }

    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanByLowonganIdAndPesertaId(String processName, String token, Integer lowonganId, Integer idPeserta, HttpServletRequest request) {
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

        try {
            // Fetch tahapan filtered by lowongan ID and peserta ID
            List<Object[]> tahapanList = tahapanSeleksiRepository.findAllTahapanByLowonganIdAndPesertaId(lowonganId, idPeserta);

            if (tahapanList == null || tahapanList.isEmpty()) {
                logger.warn(LoggerUtil.formatLog(
                        jobId, ip, "R", processName,
                        responseCodeUtil.getCode("077"),
                        responseCodeUtil.getMessage("077"),
                        Map.of("message", "No tahapan found")
                ));
                return ResponseEntity.status(400).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        "No tahapan found for lowongan ID: " + lowonganId + " and peserta ID: " + idPeserta,
                        null
                ));
            }

            logger.info(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    new ResponseWrapper<>(
                            responseCodeUtil.getCode("000"),
                            responseCodeUtil.getMessage("000"),
                            tahapanList
                    )
            ));
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    tahapanList
            ));

        } catch (Exception e) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    Map.of("message", "Internal server error while fetching progress")
            ));
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }

    public ResponseEntity<ResponseWrapper<List<Object[]>>> getPesertaProgress(String processName, String token, Integer lowonganId, String pesertaId, HttpServletRequest request) {
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

        // Extract id_peserta from the token
        Integer idPesertaFromToken = tokenUtil.extractPesertaId(token);
        if (idPesertaFromToken == null) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("403"),
                    responseCodeUtil.getMessage("403"),
                    Map.of("message", "Unauthorized: Invalid token or peserta not found.")
            ));
            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("403"),
                    "Unauthorized: Invalid token or peserta not found.",
                    null
            ));
        }

        // Verify if the pesertaId exists in peserta_lowongan and matches id_peserta from token
        boolean isAuthorized = tahapanSeleksiRepository.existsPesertaInLowongan(lowonganId, pesertaId, idPesertaFromToken);

        if (!isAuthorized) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("403"),
                    responseCodeUtil.getMessage("403"),
                    Map.of("message", "Unauthorized access. Peserta ID does not match the token.")
            ));
            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("403"),
                    "Unauthorized access. Peserta ID does not match the token.",
                    null
            ));
        }

        try {
            // Fetch progress from the repository
            List<Object[]> progressList = tahapanSeleksiRepository.getPesertaProgress(lowonganId, pesertaId);

            if (progressList == null || progressList.isEmpty()) {
                logger.warn(LoggerUtil.formatLog(
                        jobId, ip, "R", processName,
                        responseCodeUtil.getCode("400"),
                        responseCodeUtil.getMessage("400"),
                        Map.of("message", "No progress found for lowongan ID: " + lowonganId + " and peserta ID: " + pesertaId)
                ));
                return ResponseEntity.status(400).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        "No progress found for lowongan ID: " + lowonganId + " and peserta ID: " + pesertaId,
                        null
                ));
            }

            logger.info(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    new ResponseWrapper<>(
                            responseCodeUtil.getCode("000"),
                            responseCodeUtil.getMessage("000"),
                            progressList
                    )
            ));
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    progressList
            ));

        } catch (Exception e) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    Map.of("message", "Internal server error while fetching progress")
            ));
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Internal server error while fetching progress",
                    null
            ));
        }
    }


}

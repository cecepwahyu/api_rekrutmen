package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.repository.TahapanSeleksiRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanByLowonganId(String token, Integer lowonganId) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        try {
            // Fetch tahapan from the repository
            List<Object[]> tahapanList = tahapanSeleksiRepository.findAllTahapanByLowonganId(lowonganId);

            if (tahapanList == null || tahapanList.isEmpty()) {
                return ResponseEntity.status(400).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        "No tahapan found for lowongan ID: " + lowonganId,
                        null
                ));
            }

            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    tahapanList
            ));

        } catch (Exception e) {
            logger.error("Error fetching tahapan for lowongan ID {}: {}", lowonganId, e.getMessage(), e);
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
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanBySlug(String token, String slug) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        try {
            // Fetch tahapan from the repository
            List<Object[]> tahapanList = tahapanSeleksiRepository.findAllTahapanBySlug(slug);

            if (tahapanList == null || tahapanList.isEmpty()) {
                return ResponseEntity.status(400).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        "No tahapan found for Slug: " + slug,
                        null
                ));
            }

            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    tahapanList
            ));

        } catch (Exception e) {
            logger.error("Error fetching tahapan for Slug {}: {}", slug, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Internal server error while fetching tahapan",
                    null
            ));
        }
    }

    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanByLowonganIdAndPesertaId(String token, Integer lowonganId, Integer idPeserta) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        try {
            // Fetch tahapan filtered by lowongan ID and peserta ID
            List<Object[]> tahapanList = tahapanSeleksiRepository.findAllTahapanByLowonganIdAndPesertaId(lowonganId, idPeserta);

            if (tahapanList == null || tahapanList.isEmpty()) {
                return ResponseEntity.status(400).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        "No tahapan found for lowongan ID: " + lowonganId + " and peserta ID: " + idPeserta,
                        null
                ));
            }

            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    tahapanList
            ));

        } catch (Exception e) {
            logger.error("Error fetching tahapan for lowongan ID {} and peserta ID {}: {}", lowonganId, idPeserta, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }

    public ResponseEntity<ResponseWrapper<List<Object[]>>> getPesertaProgress(String token, Integer lowonganId, String pesertaId) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Extract id_peserta from the token
        Integer idPesertaFromToken = tokenUtil.extractPesertaId(token);
        if (idPesertaFromToken == null) {
            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("403"),
                    "Unauthorized: Invalid token or peserta not found.",
                    null
            ));
        }

        // Verify if the pesertaId exists in peserta_lowongan and matches id_peserta from token
        boolean isAuthorized = tahapanSeleksiRepository.existsPesertaInLowongan(lowonganId, pesertaId, idPesertaFromToken);

        if (!isAuthorized) {
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
                return ResponseEntity.status(400).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("400"),
                        "No progress found for lowongan ID: " + lowonganId + " and peserta ID: " + pesertaId,
                        null
                ));
            }

            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    progressList
            ));

        } catch (Exception e) {
            logger.error("Error fetching progress for lowongan ID {} and peserta ID {}: {}", lowonganId, pesertaId, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Internal server error while fetching progress",
                    null
            ));
        }
    }


}

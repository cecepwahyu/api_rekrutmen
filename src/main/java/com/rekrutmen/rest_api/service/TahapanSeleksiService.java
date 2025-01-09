package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.TahapanSeleksi;
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

    public ResponseEntity<ResponseWrapper<List<TahapanSeleksi>>> getTahapanSeleksi(String token) {
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

        // Fetch lowongan list ordered by idTahapan
        List<TahapanSeleksi> tahapanSeleksis = tahapanSeleksiRepository.findAllOrderedByIdTahapan();
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                tahapanSeleksis
        ));
    }

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

}

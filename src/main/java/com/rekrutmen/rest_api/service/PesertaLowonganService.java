package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.PesertaLowonganRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.LowonganPesertaDocuments;
import com.rekrutmen.rest_api.model.PesertaLowongan;
import com.rekrutmen.rest_api.repository.LowonganPesertaDocumentsRepository;
import com.rekrutmen.rest_api.repository.PesertaLowonganRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PesertaLowonganService {

    private static final Logger logger = LoggerFactory.getLogger(PesertaLowonganService.class);

    @Autowired
    private PesertaLowonganRepository pesertaLowonganRepository;

    @Autowired
    private LowonganPesertaDocumentsRepository lowonganPesertaDocumentsRepository;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private TokenUtil tokenUtil;

    public ResponseEntity<ResponseWrapper<PesertaLowongan>> handleSubmitLowongan(String token, PesertaLowonganRequest pesertaLowonganRequest) {

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

        // Create and save new PesertaLowongan record
        PesertaLowongan pesertaLowongan = new PesertaLowongan();
        pesertaLowongan.setIdLowongan(pesertaLowonganRequest.getIdLowongan());
        pesertaLowongan.setIdPeserta(pesertaLowonganRequest.getIdPeserta());
        pesertaLowongan.setStatus("Applied");
        pesertaLowongan.setTanggalAplikasi(LocalDateTime.now());
        pesertaLowongan.setLastStatusUpdate(LocalDateTime.now());

        PesertaLowongan savedPesertaLowongan = pesertaLowonganRepository.save(pesertaLowongan);

        // Insert related data into lowongan_peserta_documents
        for (Integer idUserDocument : pesertaLowonganRequest.getIdUserDocuments()) {
            LowonganPesertaDocuments lowonganPesertaDocument = new LowonganPesertaDocuments();
            lowonganPesertaDocument.setIdPesertaLowongan(savedPesertaLowongan.getId());
            lowonganPesertaDocument.setIdUserDocument(idUserDocument);
            lowonganPesertaDocument.setSudahDiverifikasi(false);
            lowonganPesertaDocument.setStatusVerifikasi("Pending");
            // Save document record
            lowonganPesertaDocumentsRepository.save(lowonganPesertaDocument);
        }

        // Log the operation
        logger.info("PesertaLowongan successfully submitted: {}", savedPesertaLowongan);

        // Create response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", savedPesertaLowongan.getId());
        responseData.put("idLowongan", savedPesertaLowongan.getIdLowongan());
        responseData.put("idPeserta", savedPesertaLowongan.getIdPeserta());
        responseData.put("status", savedPesertaLowongan.getStatus());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                savedPesertaLowongan
        ));
    }


    /**
     * Validates the token and fetches the lock status for a given idPeserta.
     *
     * @param token     the authorization token
     * @param idPeserta the idPeserta to check
     * @return the lock status response
     */
    public ResponseEntity<ResponseWrapper<String>> checkLockStatus(String token, Integer idPeserta) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Fetch lock status
        String lockStatus;
        try {
            lockStatus = pesertaLowonganRepository.findLockStatusByIdPeserta(idPeserta);
            if (lockStatus == null) {
                return ResponseEntity.status(404).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("404"),
                        "Lock status not found for idPeserta: " + idPeserta,
                        null
                ));
            }
        } catch (Exception e) {
            logger.error("Error fetching lock status for idPeserta {}: {}", idPeserta, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Internal server error while fetching lock status",
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Lock status fetched successfully",
                lockStatus
        ));
    }

}

package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.PesertaLowonganRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.LogPesertaLowongan;
import com.rekrutmen.rest_api.model.LowonganPesertaDocuments;
import com.rekrutmen.rest_api.model.PesertaLowongan;
import com.rekrutmen.rest_api.repository.LogPesertaLowonganRepository;
import com.rekrutmen.rest_api.repository.LowonganPesertaDocumentsRepository;
import com.rekrutmen.rest_api.repository.PesertaLowonganRepository;
import com.rekrutmen.rest_api.repository.PesertaRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PesertaLowonganService {

    private static final Logger logger = LoggerFactory.getLogger(PesertaLowonganService.class);

    @Autowired
    private PesertaLowonganRepository pesertaLowonganRepository;

    @Autowired
    private LowonganPesertaDocumentsRepository lowonganPesertaDocumentsRepository;

    @Autowired
    private PesertaRepository pesertaRepository;

    @Autowired
    private LogPesertaLowonganRepository logPesertaLowonganRepository;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private TokenUtil tokenUtil;

    @Transactional
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

        // Update is_final field in Peserta
        try {
            pesertaRepository.setPesertaIsFinal(pesertaLowonganRequest.getIdPeserta(), LocalDateTime.now());
            logger.info("Peserta is_final updated to true for idPeserta: {}", pesertaLowonganRequest.getIdPeserta());
        } catch (Exception e) {
            logger.error("Error updating is_final for idPeserta: {}", pesertaLowonganRequest.getIdPeserta(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Internal server error while updating is_final",
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
        pesertaLowongan.setIsRekrutmen(true);

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

    @Transactional
    public ResponseEntity<ResponseWrapper<PesertaLowongan>> handleSubmitJobdesc(String token, PesertaLowonganRequest pesertaLowonganRequest) {

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

        // Update is_final field in Peserta
        try {
            pesertaRepository.setPesertaIsFinal(pesertaLowonganRequest.getIdPeserta(), LocalDateTime.now());
            logger.info("Peserta is_final updated to true for idPeserta: {}", pesertaLowonganRequest.getIdPeserta());
        } catch (Exception e) {
            logger.error("Error updating is_final for idPeserta: {}", pesertaLowonganRequest.getIdPeserta(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Internal server error while updating is_final",
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
        pesertaLowongan.setIsRekrutmen(false);

        PesertaLowongan savedPesertaLowongan = pesertaLowonganRepository.save(pesertaLowongan);

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

    public ResponseEntity<ResponseWrapper<List<PesertaLowongan>>> getPesertaLowonganByCriteria(String token, Integer idLowongan, Integer idPeserta, Boolean isRekrutmen) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Fetch data based on the criteria
        List<PesertaLowongan> pesertaLowonganList = pesertaLowonganRepository.findByLowonganPesertaAndRekrutmen(idLowongan, idPeserta, isRekrutmen);

        if (pesertaLowonganList.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("404"),
                    "No data found for the given criteria",
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaLowonganList
        ));
    }

    // Insert peserta max age
    @Transactional
    public ResponseEntity<ResponseWrapper<LogPesertaLowongan>> logPesertaLowonganMaxAge(Integer idPeserta, Integer idLowongan) {
        try {
            LogPesertaLowongan log = new LogPesertaLowongan();
            log.setIdPeserta(idPeserta);
            log.setIdLowongan(idLowongan);
            log.setCreatedAt(LocalDateTime.now());

            // Save log entry
            LogPesertaLowongan savedLog = logPesertaLowonganRepository.save(log);

            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    savedLog
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Error inserting log record: " + e.getMessage(),
                    null
            ));
        }
    }

}

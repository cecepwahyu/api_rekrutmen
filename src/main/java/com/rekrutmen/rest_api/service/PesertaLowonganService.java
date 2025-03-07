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
import com.rekrutmen.rest_api.util.LoggerUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ResponseWrapper<PesertaLowongan>> handleSubmitLowongan(String processName, String token, PesertaLowonganRequest pesertaLowonganRequest, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

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

        // Update is_final field in Peserta
        try {
            pesertaRepository.setPesertaIsFinal(pesertaLowonganRequest.getIdPeserta(), LocalDateTime.now());
        } catch (Exception e) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    Map.of("message", "Internal server error while updating is_final")
            ));
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

        // Create response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", savedPesertaLowongan.getId());
        responseData.put("idLowongan", savedPesertaLowongan.getIdLowongan());
        responseData.put("idPeserta", savedPesertaLowongan.getIdPeserta());
        responseData.put("status", savedPesertaLowongan.getStatus());

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        responseCodeUtil.getMessage("000"),
                        savedPesertaLowongan
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                savedPesertaLowongan
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<PesertaLowongan>> handleSubmitJobdesc(String processName, String token, PesertaLowonganRequest pesertaLowonganRequest, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

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

        // Update is_final field in Peserta
//        try {
//            pesertaRepository.setPesertaIsFinal(pesertaLowonganRequest.getIdPeserta(), LocalDateTime.now());
//        } catch (Exception e) {
//            logger.warn(LoggerUtil.formatLog(
//                    jobId, ip, "R", processName,
//                    responseCodeUtil.getCode("500"),
//                    responseCodeUtil.getMessage("500"),
//                    Map.of("message", "Internal server error while updating is_final")
//            ));
//            return ResponseEntity.status(500).body(new ResponseWrapper<>(
//                    responseCodeUtil.getCode("500"),
//                    "Internal server error while updating is_final",
//                    null
//            ));
//        }

        // Create and save new PesertaLowongan record
        PesertaLowongan pesertaLowongan = new PesertaLowongan();
        pesertaLowongan.setIdLowongan(pesertaLowonganRequest.getIdLowongan());
        pesertaLowongan.setIdPeserta(pesertaLowonganRequest.getIdPeserta());
        pesertaLowongan.setStatus("Applied");
        pesertaLowongan.setTanggalAplikasi(LocalDateTime.now());
        pesertaLowongan.setLastStatusUpdate(LocalDateTime.now());
        pesertaLowongan.setIsRekrutmen(false);

        PesertaLowongan savedPesertaLowongan = pesertaLowonganRepository.save(pesertaLowongan);

        // Create response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", savedPesertaLowongan.getId());
        responseData.put("idLowongan", savedPesertaLowongan.getIdLowongan());
        responseData.put("idPeserta", savedPesertaLowongan.getIdPeserta());
        responseData.put("status", savedPesertaLowongan.getStatus());

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        responseCodeUtil.getMessage("000"),
                        savedPesertaLowongan
                )
        ));
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
    public ResponseEntity<ResponseWrapper<String>> checkLockStatus(String processName, String token, Integer idPeserta, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

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

        // Fetch lock status
        String lockStatus;
        try {
            lockStatus = pesertaLowonganRepository.findLockStatusByIdPeserta(idPeserta);
            if (lockStatus == null) {
                logger.warn(LoggerUtil.formatLog(
                        jobId, ip, "R", processName,
                        responseCodeUtil.getCode("077"),
                        responseCodeUtil.getMessage("077"),
                        Map.of("message", "Data not found")
                ));
                return ResponseEntity.status(400).body(new ResponseWrapper<>(
                        responseCodeUtil.getCode("077"),
                        "Lock status not found for idPeserta: " + idPeserta,
                        null
                ));
            }
        } catch (Exception e) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    Map.of("message", "Data not found")
            ));
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Internal server error while fetching lock status",
                    null
            ));
        }

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        responseCodeUtil.getMessage("000"),
                        lockStatus
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Lock status fetched successfully",
                lockStatus
        ));
    }

    public ResponseEntity<ResponseWrapper<List<PesertaLowongan>>> getPesertaLowonganByCriteria(String processName, String token, Integer idLowongan, Integer idPeserta, Boolean isRekrutmen, HttpServletRequest request) {
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

        // Fetch data based on the criteria
        List<PesertaLowongan> pesertaLowonganList = pesertaLowonganRepository.findByLowonganPesertaAndRekrutmen(idLowongan, idPeserta, isRekrutmen);

        if (pesertaLowonganList.isEmpty()) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Data not found")
            ));
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    null
            ));
        }

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        responseCodeUtil.getMessage("000"),
                        pesertaLowonganList
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaLowonganList
        ));
    }

    // Insert peserta max age
    @Transactional
    public ResponseEntity<ResponseWrapper<LogPesertaLowongan>> logPesertaLowonganMaxAge(String processName, String token, Integer idPeserta, Integer idLowongan, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                Map.of("token", token, "idPeserta", idPeserta, "idLowongan", idLowongan)
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
            LogPesertaLowongan log = new LogPesertaLowongan();
            log.setIdPeserta(idPeserta);
            log.setIdLowongan(idLowongan);
            log.setCreatedAt(LocalDateTime.now());

            // Save log entry
            LogPesertaLowongan savedLog = logPesertaLowonganRepository.save(log);

            logger.info(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    new ResponseWrapper<>(
                            responseCodeUtil.getCode("000"),
                            responseCodeUtil.getMessage("000"),
                            savedLog
                    )
            ));
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    savedLog
            ));
        } catch (Exception e) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    Map.of("message", "Error inserting log record")
            ));
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    "Error inserting log record: " + e.getMessage(),
                    null
            ));
        }
    }

}

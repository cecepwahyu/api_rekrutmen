package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.PesertaPengalamanRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PesertaPengalaman;
import com.rekrutmen.rest_api.repository.PesertaPengalamanRepository;
import com.rekrutmen.rest_api.util.LoggerUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PesertaPengalamanService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private PesertaPengalamanRepository pesertaPengalamanRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<List<PesertaPengalaman>>> getPesertaPengalamanDetail(String processName, String token, Integer idPeserta, HttpServletRequest request) {
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

        // Extract id_peserta from token
        Integer idPesertaFromToken = tokenUtil.extractPesertaId(token);

        if (idPesertaFromToken == null || !idPesertaFromToken.equals(idPeserta.intValue())) {
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

        // Fetch peserta details by ID Peserta
        List<PesertaPengalaman> pesertaPengalaman = pesertaPengalamanRepository.findByIdPeserta(idPeserta);

        if (pesertaPengalaman == null) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    Map.of("message", "Peserta not found")
            ));
            return ResponseEntity.status(200).body(new ResponseWrapper<>(
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
                        pesertaPengalaman
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaPengalaman
        ));
    }

    /**
     * Insert multiple PesertaPengalaman records.
     */
    public ResponseEntity<ResponseWrapper<List<PesertaPengalaman>>> insertPesertaPengalaman(
            String token, Integer idPeserta, List<PesertaPengalamanRequest> pengalamanRequests) {

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

        List<PesertaPengalaman> pengalamanEntities = new ArrayList<>();

        for (PesertaPengalamanRequest request : pengalamanRequests) {
            PesertaPengalaman pengalaman = new PesertaPengalaman();
            pengalaman.setIdPeserta(idPeserta);
            pengalaman.setNamaInstansi(request.getNamaInstansi());
            pengalaman.setPosisiKerja(request.getPosisiKerja());
            pengalaman.setPeriodeKerja(request.getPeriodeKerja());
            pengalaman.setDeskripsiKerja(request.getDeskripsiKerja());
            pengalaman.setSuratPengalamanKerja(request.getSuratPengalamanKerja());
            pengalamanEntities.add(pengalaman);
        }

        try {
            List<PesertaPengalaman> savedEntities = pesertaPengalamanRepository.saveAll(pengalamanEntities);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    savedEntities
            ));
        } catch (Exception e) {
            logger.error("Error inserting PesertaPengalaman: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }
}

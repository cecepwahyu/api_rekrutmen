package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.PesertaPengalamanRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PesertaPengalaman;
import com.rekrutmen.rest_api.repository.PesertaPengalamanRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PesertaPengalamanService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private PesertaPengalamanRepository pesertaPengalamanRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<List<PesertaPengalaman>>> getPesertaPengalamanDetail(String token, Integer idPeserta) {

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

        // Extract id_peserta from token
        Integer idPesertaFromToken = tokenUtil.extractPesertaId(token);

        if (idPesertaFromToken == null || !idPesertaFromToken.equals(idPeserta.intValue())) {
            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("403"),
                    "Unauthorized access. Peserta ID does not match the token.",
                    null
            ));
        }

        // Fetch peserta details by ID Peserta
        List<PesertaPengalaman> pesertaPengalaman = pesertaPengalamanRepository.findByIdPeserta(idPeserta);

        if (pesertaPengalaman == null) {
            return ResponseEntity.status(200).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        logger.info("Response Data = {}", pesertaPengalaman);

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

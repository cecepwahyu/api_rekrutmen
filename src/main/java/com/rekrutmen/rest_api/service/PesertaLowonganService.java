package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.PesertaLowonganRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PesertaLowongan;
import com.rekrutmen.rest_api.repository.PesertaLowonganRepository;
import com.rekrutmen.rest_api.util.JwtUtil;
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
import java.util.UUID;

@Service
public class PesertaLowonganService {

    private static final Logger logger = LoggerFactory.getLogger(PesertaLowonganService.class);

    @Autowired
    private PesertaLowonganRepository pesertaLowonganRepository;

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
        //pesertaLowongan.setTahunAplikasi(2024);

        pesertaLowonganRepository.save(pesertaLowongan);

        // Log the operation
        logger.info("PesertaLowongan successfully submitted: {}", pesertaLowongan);

        // Create response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", pesertaLowongan.getId());
        responseData.put("idLowongan", pesertaLowongan.getIdLowongan());
        responseData.put("idPeserta", pesertaLowongan.getIdPeserta());
        responseData.put("status", pesertaLowongan.getStatus());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaLowongan
        ));
    }
}

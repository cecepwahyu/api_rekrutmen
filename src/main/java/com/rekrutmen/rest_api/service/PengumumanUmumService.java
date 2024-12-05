package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.model.PengumumanUmum;
import com.rekrutmen.rest_api.repository.PengumumanUmumRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PengumumanUmumService {

    @Autowired
    private PengumumanUmumRepository pengumumanUmumRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<List<PengumumanUmum>>> getAllPengumumanUmums(String token) {

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

        // Fetch articles
        List<PengumumanUmum> pengumumanUmums = pengumumanUmumRepository.findAll();
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pengumumanUmums
        ));
    }

    public ResponseEntity<ResponseWrapper<PengumumanUmum>> getPengumumanUmumDetail(String token, UUID id) {

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

        // Fetch pengumuman umum details
        PengumumanUmum pengumumanUmum = pengumumanUmumRepository.findById(id).orElse(null);

        if (pengumumanUmum == null) {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pengumumanUmum
        ));
    }
}

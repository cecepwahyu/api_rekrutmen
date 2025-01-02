package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.model.ProgresTahapan;
import com.rekrutmen.rest_api.model.TahapanSeleksi;
import com.rekrutmen.rest_api.repository.ProgresTahapanRepository;
import com.rekrutmen.rest_api.repository.TahapanSeleksiRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TahapanSeleksiService {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;
    @Autowired
    private ProgresTahapanRepository progresTahapanRepository;
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

}

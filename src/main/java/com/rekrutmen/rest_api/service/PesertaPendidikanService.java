package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.PesertaPendidikanRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PesertaPendidikan;
import com.rekrutmen.rest_api.repository.PesertaPendidikanRepository;
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
public class PesertaPendidikanService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private PesertaPendidikanRepository pesertaPendidikanRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<List<PesertaPendidikan>>> getPesertaPendidikanDetail(String token, Integer idPeserta) {

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

        // Fetch peserta details by ID Peserta
        List<PesertaPendidikan> pesertaPendidikan = pesertaPendidikanRepository.findByIdPeserta(idPeserta);

        if (pesertaPendidikan == null) {
            return ResponseEntity.status(200).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        logger.info("Response Data = {}", pesertaPendidikan);

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaPendidikan
        ));
    }


    /**
     * Insert multiple PesertaPendidikan records.
     */
    public ResponseEntity<ResponseWrapper<List<PesertaPendidikan>>> insertPesertaPendidikan(
            String token, Integer idPeserta, List<PesertaPendidikanRequest> pendidikanRequests) {

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

        List<PesertaPendidikan> pendidikanEntities = new ArrayList<>();

        for (PesertaPendidikanRequest request : pendidikanRequests) {
            PesertaPendidikan pendidikan = new PesertaPendidikan();
            pendidikan.setIdPeserta(idPeserta);
            pendidikan.setIdJenjang(request.getIdJenjang());
            pendidikan.setNamaInstitusi(request.getNamaInstitusi());
            pendidikan.setJurusan(request.getJurusan());
            pendidikan.setThnMasuk(request.getThnMasuk());
            pendidikan.setThnLulus(request.getThnLulus());
            pendidikan.setNilai(request.getNilai());
            pendidikan.setGelar(request.getGelar());
            pendidikan.setAchievements(request.getAchievements());
            pendidikanEntities.add(pendidikan);
        }

        try {
            List<PesertaPendidikan> savedEntities = pesertaPendidikanRepository.saveAll(pendidikanEntities);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    savedEntities
            ));
        } catch (Exception e) {
            logger.error("Error inserting PesertaPendidikan: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }
}

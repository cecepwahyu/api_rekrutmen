package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PesertaPendidikan;
import com.rekrutmen.rest_api.model.PesertaPengalaman;
import com.rekrutmen.rest_api.repository.PesertaPendidikanRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PesertaPendidikanService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private PesertaPendidikanRepository pesertaPendidikanRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<PesertaPendidikan>> getPesertaPendidikanDetail(String token, Integer idPeserta) {

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
        PesertaPendidikan pesertaPendidikan = pesertaPendidikanRepository.findByIdPeserta(idPeserta).orElse(null);

        if (pesertaPendidikan == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        logger.info(
                "Response Data = {\"responseCode\": \"{}\", \"responseMessage\": \"{}\", \"data\": {\"idPendidikan\": \"{}\", \"idPeserta\": \"{}\", \"idJenjang\": \"{}\", \"namaInstitusi\": \"{}\", \"jurusan\": \"{}\", \"thnMasuk\": \"{}\" \"thnLulus\": \"{}\" \"nilai\": \"{}\" \"gelar\": \"{}\" \"achievements\": \"{}\"}}",
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaPendidikan.getIdPendidikan(),
                pesertaPendidikan.getIdPeserta(),
                pesertaPendidikan.getIdJenjang(),
                pesertaPendidikan.getNamaInstitusi(),
                pesertaPendidikan.getJurusan(),
                pesertaPendidikan.getThnMasuk(),
                pesertaPendidikan.getThnLulus(),
                pesertaPendidikan.getNilai(),
                pesertaPendidikan.getGelar(),
                pesertaPendidikan.getAchievements()
        );

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaPendidikan
        ));
    }
}

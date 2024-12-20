package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PesertaKontak;
import com.rekrutmen.rest_api.repository.PesertaKontakRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PesertaKontakService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private PesertaKontakRepository pesertaKontakRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<PesertaKontak>> getPesertaKontakDetail(String token, Integer idPeserta) {

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
        PesertaKontak pesertaKontak = pesertaKontakRepository.findByIdPeserta(idPeserta).orElse(null);

        if (pesertaKontak == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        logger.info(
                "Response Data = {\"responseCode\": \"{}\", \"responseMessage\": \"{}\", \"data\": {\"idKontakPeserta\": \"{}\", \"idPeserta\": \"{}\", \"namaKontak\": \"{}\", \"hubKontak\": \"{}\", \"telpKontak\": \"{}\", \"emailKontak\": \"{}\", \"alamatKontak\": \"{}\"}}",
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaKontak.getIdKontakPeserta(),
                pesertaKontak.getIdPeserta(),
                pesertaKontak.getNamaKontak(),
                pesertaKontak.getHubKontak(),
                pesertaKontak.getTelpKontak(),
                pesertaKontak.getEmailKontak(),
                pesertaKontak.getAlamatKontak()
        );

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaKontak
        ));
    }
}

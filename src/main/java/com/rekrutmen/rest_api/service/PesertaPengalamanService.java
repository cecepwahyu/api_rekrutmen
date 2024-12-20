package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.model.PesertaPengalaman;
import com.rekrutmen.rest_api.repository.PesertaPengalamanRepository;
import com.rekrutmen.rest_api.util.MaskingUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PesertaPengalamanService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private PesertaPengalamanRepository pesertaPengalamanRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<PesertaPengalaman>> getPesertaPengalamanDetail(String token, Integer idPeserta) {

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
        PesertaPengalaman pesertaPengalaman = pesertaPengalamanRepository.findByIdPeserta(idPeserta).orElse(null);

        if (pesertaPengalaman == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        logger.info(
                "Response Data = {\"responseCode\": \"{}\", \"responseMessage\": \"{}\", \"data\": {\"idDataKerja\": \"{}\", \"idPeserta\": \"{}\", \"namaInstansi\": \"{}\", \"posisiKerja\": \"{}\", \"periodeKerja\": \"{}\", \"deskripsiKerja\": \"{}\"}}",
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaPengalaman.getIdDataKerja(),
                pesertaPengalaman.getIdPeserta(),
                pesertaPengalaman.getNamaInstansi(),
                pesertaPengalaman.getPosisiKerja(),
                pesertaPengalaman.getPeriodeKerja(),
                pesertaPengalaman.getDeskripsiKerja()
        );

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                pesertaPengalaman
        ));
    }
}

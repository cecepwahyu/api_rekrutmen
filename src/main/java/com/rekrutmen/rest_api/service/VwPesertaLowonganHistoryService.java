package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.VwPesertaLowonganHistory;
import com.rekrutmen.rest_api.repository.VwPesertaLowonganHistoryRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VwPesertaLowonganHistoryService {

    @Autowired
    private VwPesertaLowonganHistoryRepository vwPesertaLowonganHistoryRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<List<VwPesertaLowonganHistory>>> getAllHistory(String token) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Fetch all history records
        List<VwPesertaLowonganHistory> historyList = vwPesertaLowonganHistoryRepository.findAll();
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                historyList
        ));
    }

    public ResponseEntity<ResponseWrapper<VwPesertaLowonganHistory>> getHistoryByIdPeserta(String token, Long idPeserta) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Fetch history by idPeserta
        Optional<VwPesertaLowonganHistory> history = vwPesertaLowonganHistoryRepository.findByIdPeserta(idPeserta);
        if (history.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("404"),
                    "Data not found",
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                history.get()
        ));
    }

    public ResponseEntity<ResponseWrapper<VwPesertaLowonganHistory>> getHistoryBySlug(String token, String slug) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Fetch history by idPeserta
        Optional<VwPesertaLowonganHistory> history = vwPesertaLowonganHistoryRepository.findBySlug(slug);
        if (history.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("404"),
                    "Data not found",
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                history.get()
        ));
    }

    public ResponseEntity<ResponseWrapper<List<VwPesertaLowonganHistory>>> getHistoryByTahun(String token, Integer tahunAplikasi) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Fetch history by tahunAplikasi
        List<VwPesertaLowonganHistory> historyList = vwPesertaLowonganHistoryRepository.findAllByTahunAplikasi(tahunAplikasi);
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                historyList
        ));
    }
}

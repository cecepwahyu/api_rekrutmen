package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.ProgresTahapan;
import com.rekrutmen.rest_api.repository.ProgresTahapanRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgresTahapanService {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private ProgresTahapanRepository progresTahapanRepository;

    public ResponseEntity<ResponseWrapper<ProgresTahapan>> getProgresTahapanDetail(String token, Integer id) {
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

        // Fetch lowongan details
        ProgresTahapan progresTahapan = progresTahapanRepository.findById(id).orElse(null);

        if (progresTahapan == null) {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                progresTahapan
        ));
    }

    public ResponseEntity<ResponseWrapper<List<ProgresTahapan>>> getProgresTahapanByIdLowongan(String token, Integer idLowongan) {
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

        // Fetch progres tahapan by idLowongan and remove duplicates
        List<ProgresTahapan> progresTahapanList = progresTahapanRepository.findByIdLowongan(idLowongan).stream()
                .distinct() // Remove exact duplicates
                .collect(Collectors.groupingBy(ProgresTahapan::getCurrentSortOrder)) // Group by currentSortOrder
                .values()
                .stream()
                .map(list -> list.get(0)) // Get the first entry in each group
                .collect(Collectors.toList());

        if (progresTahapanList.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                progresTahapanList
        ));
    }
}

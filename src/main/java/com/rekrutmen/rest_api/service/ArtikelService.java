package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.repository.ArtikelRepository;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtikelService {

    @Autowired
    private ArtikelRepository artikelRepository;

    @Autowired
    private TokenUtil tokenUtil;

    public ResponseEntity<ResponseWrapper<List<Artikel>>> getArtikelList(String token) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    "401",
                    "Unauthorized",
                    null
            ));
        }

        // Fetch articles
        List<Artikel> artikels = artikelRepository.findAll();
        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Success",
                artikels
        ));
    }
}

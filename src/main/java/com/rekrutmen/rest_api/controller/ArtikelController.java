package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.service.ArtikelService;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/artikel")
public class ArtikelController {

    @Autowired
    private ArtikelService artikelService;

    @Autowired
    private TokenUtil tokenUtil;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper<List<Artikel>>> getArtikelList(
            @RequestHeader("Authorization") String token
    ) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    "401",
                    "Unauthorized",
                    null
            ));
        }

        // Fetch articles
        List<Artikel> artikels = artikelService.getAllArtikels();
        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Success",
                artikels
        ));
    }
}

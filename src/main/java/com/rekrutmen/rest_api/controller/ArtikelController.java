package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.service.ArtikelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/artikel")
public class ArtikelController {

    @Autowired
    private ArtikelService artikelService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper<List<Artikel>>> getArtikelList(@RequestHeader("Authorization") String token) {
        return artikelService.getArtikelList(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Artikel>> getArtikelDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID id) {
        return artikelService.getArtikelDetail(token, id);
    }
}

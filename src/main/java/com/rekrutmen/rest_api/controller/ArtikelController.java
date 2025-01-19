package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
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

    @GetMapping("/paginated")
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedArticles(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page) {
        return artikelService.getPaginatedArticles(token, page);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseWrapper<Artikel>> getArtikelDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID id) {
        return artikelService.getArtikelDetail(token, id);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ResponseWrapper<Artikel>> getArtikelDetailSlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug) {
        return artikelService.getArtikelDetailSlug(token, slug);
    }

    @GetMapping("/image/{gambar}")
    public ResponseEntity<?> getArticleImage(@PathVariable String gambar) {
        return artikelService.getArticleImage(gambar);
    }


}

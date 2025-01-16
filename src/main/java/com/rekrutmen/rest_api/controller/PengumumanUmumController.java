package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PengumumanUmum;
import com.rekrutmen.rest_api.service.PengumumanUmumService;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pengumuman-umum")
public class PengumumanUmumController {

    @Autowired
    private PengumumanUmumService pengumumanUmumService;

    @Autowired
    private TokenUtil tokenUtil;

    @GetMapping("/paginated")
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedPengumumanUmums(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page) {
        return pengumumanUmumService.getPaginatedPengumumanUmums(token, page);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ResponseWrapper<PengumumanUmum>> getArtikelDetailSlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug) {
        return pengumumanUmumService.getPengumumanUmumDetailSlug(token, slug);
    }
}

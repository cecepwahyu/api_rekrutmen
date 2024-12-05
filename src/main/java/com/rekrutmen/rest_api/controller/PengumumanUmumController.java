package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
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

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper<List<PengumumanUmum>>> getPengumumanUmumList(@RequestHeader("Authorization") String token) {
        return pengumumanUmumService.getAllPengumumanUmums(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<PengumumanUmum>> getPengumumanUmumDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID id) {
        return pengumumanUmumService.getPengumumanUmumDetail(token, id);
    }
}

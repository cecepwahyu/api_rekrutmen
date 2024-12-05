package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PengumumanUmum;
import com.rekrutmen.rest_api.service.PengumumanUmumService;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}

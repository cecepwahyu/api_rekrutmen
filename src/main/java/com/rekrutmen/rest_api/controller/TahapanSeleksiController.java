package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.model.ProgresTahapan;
import com.rekrutmen.rest_api.model.TahapanSeleksi;
import com.rekrutmen.rest_api.service.ProgresTahapanService;
import com.rekrutmen.rest_api.service.TahapanSeleksiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tahapan")
public class TahapanSeleksiController {

    @Autowired
    private TahapanSeleksiService tahapanSeleksiService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper<List<TahapanSeleksi>>> getTahapanSeleksi(@RequestHeader("Authorization") String token) {
        return tahapanSeleksiService.getTahapanSeleksi(token);
    }

}

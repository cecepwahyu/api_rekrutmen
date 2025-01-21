package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.model.Provinsi;
import com.rekrutmen.rest_api.service.ProvinsiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provinsi")
public class ProvinsiController {

    @Autowired
    private ProvinsiService provinsiService;

    @GetMapping("/list")
    public ResponseEntity<List<Provinsi>> getAllProvinsi() {
        List<Provinsi> provinsiList = provinsiService.getAllProvinsi();
        return ResponseEntity.ok(provinsiList);
    }
}

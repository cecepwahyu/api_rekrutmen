package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.model.Kabupaten;
import com.rekrutmen.rest_api.service.KabupatenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kabupaten")
public class KabupatenController {

    @Autowired
    private KabupatenService kabupatenService;

    @GetMapping("/list")
    public ResponseEntity<List<Kabupaten>> getAllKabupaten() {
        List<Kabupaten> kabupatenList = kabupatenService.getAllKabupaten();
        return ResponseEntity.ok(kabupatenList);
    }

    @GetMapping("/by-provinsi/{kodeProvinsi}")
    public ResponseEntity<List<Kabupaten>> getKabupatenByProvinsi(@PathVariable String kodeProvinsi) {
        List<Kabupaten> kabupatenList = kabupatenService.getKabupatenByProvinsi(kodeProvinsi);
        return ResponseEntity.ok(kabupatenList);
    }
}

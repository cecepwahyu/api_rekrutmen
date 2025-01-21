package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.model.Kecamatan;
import com.rekrutmen.rest_api.service.KecamatanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kecamatan")
public class KecamatanController {

    @Autowired
    private KecamatanService kecamatanService;

    @GetMapping("/list")
    public ResponseEntity<List<Kecamatan>> getAllKecamatan() {
        List<Kecamatan> kecamatanList = kecamatanService.getAllKecamatan();
        return ResponseEntity.ok(kecamatanList);
    }

    @GetMapping("/by-kabupaten/{kodeKabupaten}")
    public ResponseEntity<List<Kecamatan>> getKecamatanByKabupaten(@PathVariable String kodeKabupaten) {
        List<Kecamatan> kecamatanList = kecamatanService.getKecamatanByKabupaten(kodeKabupaten);
        return ResponseEntity.ok(kecamatanList);
    }
}

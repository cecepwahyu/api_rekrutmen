package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.model.Desa;
import com.rekrutmen.rest_api.service.DesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desa")
public class DesaController {

    @Autowired
    private DesaService desaService;

    @GetMapping("/list")
    public ResponseEntity<List<Desa>> getAllDesa() {
        List<Desa> desaList = desaService.getAllDesa();
        return ResponseEntity.ok(desaList);
    }

    @GetMapping("/by-kecamatan/{kodeKecamatan}")
    public ResponseEntity<List<Desa>> getDesaByKecamatan(@PathVariable String kodeKecamatan) {
        List<Desa> desaList = desaService.getDesaByKecamatan(kodeKecamatan);
        return ResponseEntity.ok(desaList);
    }
}

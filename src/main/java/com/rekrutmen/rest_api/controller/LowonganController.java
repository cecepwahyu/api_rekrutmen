package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.service.LowonganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lowongan")
public class LowonganController {

    @Autowired
    private LowonganService lowonganService;

    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper<List<Lowongan>>> getLowonganList(@RequestHeader("Authorization") String token) {
        return lowonganService.getLowonganList(token);
    }

    @GetMapping("/paginated")
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongans(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page) {
        return lowonganService.getPaginatedLowongans(token, page);
    }

    @GetMapping("/{idLowongan}")
    public ResponseEntity<ResponseWrapper<Lowongan>> getLowonganDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Long idLowongan) {
        return lowonganService.getLowonganDetail(token, idLowongan);
    }

}

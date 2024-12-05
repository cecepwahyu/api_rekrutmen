package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.service.LowonganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

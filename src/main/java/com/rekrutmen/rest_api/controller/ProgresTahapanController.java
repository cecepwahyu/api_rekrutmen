package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.ProgresTahapan;
import com.rekrutmen.rest_api.service.ProgresTahapanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgresTahapanController {

    @Autowired
    private ProgresTahapanService progresTahapanService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ProgresTahapan>> getLowonganDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        return progresTahapanService.getProgresTahapanDetail(token, id);
    }

}

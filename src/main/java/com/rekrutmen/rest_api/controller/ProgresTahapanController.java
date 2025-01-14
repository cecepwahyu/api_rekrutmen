package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.ProgresTahapan;
import com.rekrutmen.rest_api.service.ProgresTahapanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgresTahapanController {

    @Autowired
    private ProgresTahapanService progresTahapanService;

    /**
     * Fetches the progress detail for a specific id.
     *
     * @param token the authorization token.
     * @param id    the id of the progress to fetch.
     * @return ResponseEntity containing the progress detail.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ProgresTahapan>> getProgresTahapanDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        return progresTahapanService.getProgresTahapanDetail(token, id);
    }

    /**
     * Fetches the progress detail by id_tahapan.
     *
     * @param token      the authorization token.
     * @param idLowongan the id_tahapan to fetch the progress for.
     * @return ResponseEntity containing the progress detail.
     */
    @GetMapping("/tahapan/{idLowongan}")
    public ResponseEntity<ResponseWrapper<List<ProgresTahapan>>> getProgresTahapanByIdTahapan(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idLowongan) {
        return progresTahapanService.getProgresTahapanByIdLowongan(token, idLowongan);
    }
}

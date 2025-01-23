package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.TahapanSeleksi;
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

    /**
     * Fetches all tahapan for a specific lowongan.
     *
     * @param token      the authorization token.
     * @param lowonganId the id of the lowongan.
     * @return ResponseEntity containing the list of tahapan.
     */
//    @GetMapping("/lowongan/id/{lowonganId}/tahapan")
//    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanByLowongan(
//            @RequestHeader("Authorization") String token,
//            @PathVariable Integer lowonganId) {
//        return tahapanSeleksiService.getTahapanByLowonganId(token, lowonganId);
//    }

    /**
     * Fetches all tahapan for a specific lowongan.
     *
     * @param token      the authorization token.
     * @param slug the id of the lowongan.
     * @return ResponseEntity containing the list of tahapan.
     */
    @GetMapping("/lowongan/slug/{slug}/tahapan")
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanBySlugLowongan(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug) {
        return tahapanSeleksiService.getTahapanBySlug(token, slug);
    }

    @GetMapping("/lowongan/id/{lowonganId}/tahapan")
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanByLowonganId(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer lowonganId,
            @RequestParam(value = "id_peserta", required = false) Integer idPeserta) {

        if (idPeserta != null) {
            return tahapanSeleksiService.getTahapanByLowonganIdAndPesertaId(token, lowonganId, idPeserta);
        }

        return tahapanSeleksiService.getTahapanByLowonganId(token, lowonganId);
    }

}

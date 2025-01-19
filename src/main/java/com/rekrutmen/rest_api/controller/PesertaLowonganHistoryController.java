package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.VwPesertaLowonganHistory;
import com.rekrutmen.rest_api.service.VwPesertaLowonganHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class PesertaLowonganHistoryController {

    @Autowired
    private VwPesertaLowonganHistoryService vwPesertaLowonganHistoryService;

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<VwPesertaLowonganHistory>>> getAllHistory(
            @RequestHeader("Authorization") String token) {
        return vwPesertaLowonganHistoryService.getAllHistory(token);
    }

    @GetMapping("/peserta/id/{idPeserta}")
    public ResponseEntity<ResponseWrapper<VwPesertaLowonganHistory>> getHistoryByIdPeserta(
            @RequestHeader("Authorization") String token,
            @PathVariable Long idPeserta) {
        return vwPesertaLowonganHistoryService.getHistoryByIdPeserta(token, idPeserta);
    }

    @GetMapping("/tahun/{tahunAplikasi}")
    public ResponseEntity<ResponseWrapper<List<VwPesertaLowonganHistory>>> getHistoryByTahun(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tahunAplikasi) {
        return vwPesertaLowonganHistoryService.getHistoryByTahun(token, tahunAplikasi);
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseWrapper<List<VwPesertaLowonganHistory>>> getHistoryByIsRekrutmenAndIdPeserta(
            @RequestHeader("Authorization") String token,
            @RequestParam Boolean isRekrutmen,
            @RequestParam Long idPeserta) {
        return vwPesertaLowonganHistoryService.getHistoryByIsRekrutmenAndIdPeserta(token, isRekrutmen, idPeserta);
    }


    @GetMapping("/peserta/slug/{slug}")
    public ResponseEntity<ResponseWrapper<VwPesertaLowonganHistory>> getHistoryBySlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug) {
        return vwPesertaLowonganHistoryService.getHistoryBySlug(token, slug);
    }
}

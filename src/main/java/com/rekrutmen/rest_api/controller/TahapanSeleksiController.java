package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.TahapanSeleksi;
import com.rekrutmen.rest_api.service.TahapanSeleksiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tahapan")
public class TahapanSeleksiController {

    @Autowired
    private TahapanSeleksiService tahapanSeleksiService;

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
            @PathVariable String slug,
            HttpServletRequest request) {
        String processName = "GET_TAHAPAN";
        return tahapanSeleksiService.getTahapanBySlug(processName, token, slug, request);
    }

    @GetMapping("/lowongan/id/{lowonganId}/tahapan")
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getTahapanByLowonganId(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer lowonganId,
            @RequestParam(value = "id_peserta", required = false) Integer idPeserta,
            HttpServletRequest request) {
        String processName = "GET_TAHAPAN";
        if (idPeserta != null) {
            return tahapanSeleksiService.getTahapanByLowonganIdAndPesertaId(processName, token, lowonganId, idPeserta, request);
        }

        return tahapanSeleksiService.getTahapanByLowonganId(processName, token, lowonganId, request);
    }

    @GetMapping("/lowongan/{lowonganId}/peserta/{pesertaId}/progress")
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getPesertaProgress(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer lowonganId,
            @PathVariable String pesertaId,
            HttpServletRequest request) {
        String processName = "PROGRESS_TAHAPAN";
        return tahapanSeleksiService.getPesertaProgress(processName, token, lowonganId, pesertaId, request);
    }

}

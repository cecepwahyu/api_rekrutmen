package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.PesertaLowonganRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.LogPesertaLowongan;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.model.PesertaLowongan;
import com.rekrutmen.rest_api.service.LowonganService;
import com.rekrutmen.rest_api.service.PesertaLowonganService;
import com.rekrutmen.rest_api.service.VwLowonganDokumenService;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lowongan")
public class LowonganController {

    @Autowired
    private LowonganService lowonganService;

    @Autowired
    private PesertaLowonganService pesertaLowonganService;

    @Autowired
    private VwLowonganDokumenService vwLowonganDokumenService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @GetMapping("/paginated")
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongans(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page) {
        return lowonganService.getPaginatedLowongans(token, page);
    }

    @GetMapping("/rekrutmen/paginated")
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongansRekrutmen(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page) {
        return lowonganService.getPaginatedLowongansRekrutmen(token, page);
    }

    @GetMapping("/jobdesc/paginated")
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongansJobDesc(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page) {
        return lowonganService.getPaginatedLowongansJobDesc(token, page);
    }

    @GetMapping("/id/{idLowongan}")
    public ResponseEntity<ResponseWrapper<Lowongan>> getLowonganDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Long idLowongan) {
        return lowonganService.getLowonganDetail(token, idLowongan);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ResponseWrapper<Lowongan>> getLowonganDetailSlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug) {
        return lowonganService.getLowonganDetailSlug(token, slug);
    }

    /**
     * Endpoint to check lock status for a specific idPeserta.
     */
    @GetMapping("/lock-status/{idPeserta}")
    public ResponseEntity<ResponseWrapper<String>> checkLockStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta) {
        return pesertaLowonganService.checkLockStatus(token, idPeserta);
    }

    @PostMapping("/slug/{slug}/apply")
    public ResponseEntity<ResponseWrapper<PesertaLowongan>> applyToLowonganBySlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug,
            @RequestBody PesertaLowonganRequest pesertaLowonganRequest) {
        // Fetch lowongan details using slug
        ResponseEntity<ResponseWrapper<Lowongan>> lowonganResponse = lowonganService.getLowonganDetailSlug(token, slug);

        if (lowonganResponse.getStatusCode().isError() || lowonganResponse.getBody() == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Lowongan not found",
                    null
            ));
        }

        // Set the idLowongan in PesertaLowonganRequest based on the fetched Lowongan
        Lowongan lowongan = lowonganResponse.getBody().getData();
        pesertaLowonganRequest.setIdLowongan(lowongan.getIdLowongan());

        // Submit the application
        return pesertaLowonganService.handleSubmitLowongan(token, pesertaLowonganRequest);
    }

    @PostMapping("/slug/{slug}/applyJobdesc")
    public ResponseEntity<ResponseWrapper<PesertaLowongan>> applyToJobdescBySlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug,
            @RequestBody PesertaLowonganRequest pesertaLowonganRequest) {
        // Fetch lowongan details using slug
        ResponseEntity<ResponseWrapper<Lowongan>> lowonganResponse = lowonganService.getLowonganDetailSlug(token, slug);

        if (lowonganResponse.getStatusCode().isError() || lowonganResponse.getBody() == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Lowongan not found",
                    null
            ));
        }

        // Set the idLowongan in PesertaLowonganRequest based on the fetched Lowongan
        Lowongan lowongan = lowonganResponse.getBody().getData();
        pesertaLowonganRequest.setIdLowongan(lowongan.getIdLowongan());

        // Submit the application
        return pesertaLowonganService.handleSubmitJobdesc(token, pesertaLowonganRequest);
    }

    @GetMapping("/dokumen/slug/{slug}")
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getDokumenBySlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug) {
        return vwLowonganDokumenService.getDokumenBySlug(token, slug);
    }

    @PostMapping("/log/max-age")
    public ResponseEntity<ResponseWrapper<LogPesertaLowongan>> logPesertaLowonganMaxAge(
            @RequestParam Integer idPeserta,
            @RequestParam Integer idLowongan) {
        return pesertaLowonganService.logPesertaLowonganMaxAge(idPeserta, idLowongan);
    }

}

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
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/rekrutmen/all")
    public ResponseEntity<ResponseWrapper<List<Lowongan>>> getAllLowongans(
            @RequestHeader("Authorization") String token,
            HttpServletRequest request) {
        String processName = "REKRUTMEN_ALL";
        return lowonganService.getAllLowongans(processName, token, request);
    }

    @GetMapping("/jobdesc/all")
    public ResponseEntity<ResponseWrapper<List<Lowongan>>> getAllJobDesc(
            @RequestHeader("Authorization") String token,
            HttpServletRequest request) {
        String processName = "JOBDESC_ALL";
        return lowonganService.getAllJobDesc(processName, token, request);
    }

    @GetMapping("/rekrutmen/paginated")
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongansRekrutmen(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            HttpServletRequest request) {
        String processName = "REKRUTMEN_LIST";
        return lowonganService.getPaginatedLowongansRekrutmen(processName, token, page, request);
    }

    @GetMapping("/jobdesc/paginated")
    public ResponseEntity<ResponseWrapper<Object>> getPaginatedLowongansJobDesc(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            HttpServletRequest request) {
        String processName = "JOBDESC_LIST";
        return lowonganService.getPaginatedLowongansJobDesc(processName, token, page, request);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ResponseWrapper<Lowongan>> getLowonganDetailSlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug,
            HttpServletRequest request) {
        String processName = "JOB_DETAIL";
        return lowonganService.getLowonganDetailSlug(processName, token, slug, request);
    }

    /**
     * Endpoint to check lock status for a specific idPeserta.
     */
    @GetMapping("/lock-status/{idPeserta}")
    public ResponseEntity<ResponseWrapper<String>> checkLockStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "LOCK_STATUS";
        return pesertaLowonganService.checkLockStatus(processName, token, idPeserta, request);
    }

    @PostMapping("/slug/{slug}/apply")
    public ResponseEntity<ResponseWrapper<PesertaLowongan>> applyToLowonganBySlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug,
            @RequestBody PesertaLowonganRequest pesertaLowonganRequest,
            HttpServletRequest request) {
        String processName = "APPLY_REKRUTMEN";
        // Fetch lowongan details using slug
        ResponseEntity<ResponseWrapper<Lowongan>> lowonganResponse = lowonganService.getLowonganDetailSlug(processName, token, slug, request);

        // Set the idLowongan in PesertaLowonganRequest based on the fetched Lowongan
        Lowongan lowongan = lowonganResponse.getBody().getData();
        pesertaLowonganRequest.setIdLowongan(lowongan.getIdLowongan());

        // Submit the application
        return pesertaLowonganService.handleSubmitLowongan(processName, token, pesertaLowonganRequest, request);
    }

    @PostMapping("/slug/{slug}/applyJobdesc")
    public ResponseEntity<ResponseWrapper<PesertaLowongan>> applyToJobdescBySlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug,
            @RequestBody PesertaLowonganRequest pesertaLowonganRequest,
            HttpServletRequest request) {
        String processName = "APPLY_JOBDESC";
        // Fetch lowongan details using slug
        ResponseEntity<ResponseWrapper<Lowongan>> lowonganResponse = lowonganService.getLowonganDetailSlug(processName, token, slug, request);

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
        return pesertaLowonganService.handleSubmitJobdesc(processName, token, pesertaLowonganRequest, request);
    }

    @GetMapping("/dokumen/slug/{slug}")
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getDokumenBySlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug,
            HttpServletRequest request) {
        String processName = "GET_DOCUMENT";
        return vwLowonganDokumenService.getDokumenBySlug(processName, token, slug, request);
    }

    @PostMapping("/log/max-age")
    public ResponseEntity<ResponseWrapper<LogPesertaLowongan>> logPesertaLowonganMaxAge(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer idPeserta,
            @RequestParam Integer idLowongan,
            HttpServletRequest request) {
        String processName = "GET_MAX_AGE";
        return pesertaLowonganService.logPesertaLowonganMaxAge(processName, token, idPeserta, idLowongan, request);
    }

}

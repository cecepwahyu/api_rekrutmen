package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.PesertaLowonganRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.model.PesertaLowongan;
import com.rekrutmen.rest_api.service.LowonganService;
import com.rekrutmen.rest_api.service.PesertaLowonganService;
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

    @PostMapping("/slug/{slug}/apply")
    public ResponseEntity<ResponseWrapper<PesertaLowongan>> applyToLowonganBySlug(
            @RequestHeader("Authorization") String token,
            @PathVariable String slug,
            @RequestBody PesertaLowonganRequest pesertaLowonganRequest) {
        // Fetch lowongan details using slug
        ResponseEntity<ResponseWrapper<Lowongan>> lowonganResponse = lowonganService.getLowonganDetailSlug(token, slug);

        if (lowonganResponse.getStatusCode().isError() || lowonganResponse.getBody() == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    "400",
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
}

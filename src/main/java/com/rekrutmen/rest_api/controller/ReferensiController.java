package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Referensi;
import com.rekrutmen.rest_api.service.ReferensiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/referensi")
public class ReferensiController {

    @Autowired
    private ReferensiService referensiService;

    @GetMapping("/value")
    public ResponseEntity<ResponseWrapper<List<Referensi>>> getReferensi(
            @RequestHeader("Authorization") String token,
            @RequestParam String refGroup1,
            @RequestParam(required = false) String refCode,
            HttpServletRequest request
    ) {
        String processName = "REFERENSI";
        return referensiService.getReferensiByGroup(processName, token, refGroup1, request);
    }
}

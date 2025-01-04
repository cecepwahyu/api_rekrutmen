package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.TahapanAnnouncementRequest;
import com.rekrutmen.rest_api.service.TahapanAnnouncementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/announcements")
public class TahapanAnnouncementsController {

    private final TahapanAnnouncementsService service;

    @Autowired
    public TahapanAnnouncementsController(TahapanAnnouncementsService service) {
        this.service = service;
    }

    @PostMapping("/content")
    public ResponseEntity<String> getContentByIdLowongan(@RequestBody TahapanAnnouncementRequest request) {
        String content = service.getContentByIdLowongan(request.getIdLowongan());
        if (content != null) {
            return ResponseEntity.ok(content);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

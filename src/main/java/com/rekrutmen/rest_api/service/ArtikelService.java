package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.repository.ArtikelRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.SFTPClient;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ArtikelService {

    @Autowired
    private ArtikelRepository artikelRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<Object>> getPaginatedArticles(String token, Integer page) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    null
            ));
        }

        // Handle null or negative page numbers gracefully
        if (page == null || page < 0) {
            page = 0; // Default to the first page
        }

        // Create a pageable object with the desired page and size
        Pageable pageable = PageRequest.of(page, 6); // 6 articles per page

        // Fetch articles with pagination
        Page<Artikel> artikelsPage = artikelRepository.findPublishedAndApproved(pageable);

        // Create a response wrapper with pagination info
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("content", artikelsPage.getContent()); // The articles for the requested page
        responseData.put("currentPage", artikelsPage.getNumber());
        responseData.put("totalItems", artikelsPage.getTotalElements());
        responseData.put("totalPages", artikelsPage.getTotalPages());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                responseData
        ));
    }

    //Get artikel by ID
    public ResponseEntity<ResponseWrapper<Artikel>> getArtikelDetail(String token, UUID id) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    null
            ));
        }

        // Fetch article details by ID
        Artikel artikel = artikelRepository.findById(id).orElse(null);

        if (artikel == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                artikel
        ));
    }

    //Get artikel by Slug
    public ResponseEntity<ResponseWrapper<Artikel>> getArtikelDetailSlug(String token, String slug) {
        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    null
            ));
        }

        // Fetch article details by ID
        Artikel artikel = artikelRepository.findBySlug(slug).orElse(null);

        if (artikel == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                artikel
        ));
    }

    public ResponseEntity<?> getArticleImage(String gambar) {

        // Find the article by gambar
        Artikel artikel = artikelRepository.findByGambar(gambar)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        // Get the file path from the `gambar` column
        String fileName = artikel.getGambar();
        String filePath = "/home/devftp/data/karir/public/uploads/artikel/" + fileName;

        // Log the file path
        System.out.println("Resolved file path: " + filePath);

        // Fetch the image from the FTP server
        try (SFTPClient sftpClient = new SFTPClient("192.168.4.79", "devftp", "devftp")) {
            byte[] imageBytes = sftpClient.downloadFile(filePath);

            // Return the image as a response
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging
            return ResponseEntity.status(500).body("Failed to fetch image: " + e.getMessage());
        }
    }


}

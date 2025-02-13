package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.PesertaInfoRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.repository.PesertaRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PesertaService {

    @Autowired
    private PesertaRepository pesertaRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    public PesertaService(PesertaRepository pesertaRepository) {
        this.pesertaRepository = pesertaRepository;
    }

    public Optional<Peserta> getProfileByIdPeserta(Integer idPeserta) {
        return pesertaRepository.findByIdPeserta(idPeserta);
    }

    public boolean isEmailTaken(String email) {
        return pesertaRepository.existsByEmail(email);
    }

    public boolean isTelpTaken(String telp, Integer currentPesertaId) {
        Optional<Peserta> existingPeserta = pesertaRepository.findByTelp(telp);

        if (existingPeserta.isEmpty()) {
            return false;
        }

        // If currentPesertaId is null (new registration), treat as duplicate
        if (currentPesertaId == null) {
            return true;
        }

        // Check if the telp belongs to the same Peserta
        return !existingPeserta.get().getIdPeserta().equals(currentPesertaId);
    }


    public Peserta registerUser(Peserta user) {
        return pesertaRepository.save(user);
    }

    public boolean isNoIdentitasExist(String noIdentitas) {
        return pesertaRepository.existsByNoIdentitas(noIdentitas);
    }

    public Peserta saveUser(Peserta peserta) {
        return pesertaRepository.save(peserta);
    }

    public ResponseEntity<ResponseWrapper<Peserta>> getPesertaDetail(String token, Integer idPeserta) {
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

        // Extract id_peserta from token
        Integer idPesertaFromToken = tokenUtil.extractPesertaId(token);

        if (idPesertaFromToken == null || !idPesertaFromToken.equals(idPeserta.intValue())) {
            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("403"),
                    "Unauthorized access. Peserta ID does not match the token.",
                    null
            ));
        }

        // Fetch peserta details by ID Peserta
        Peserta peserta = pesertaRepository.findByIdPeserta(idPeserta).orElse(null);

        if (peserta == null) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                peserta
        ));
    }


    public ResponseEntity<ResponseWrapper<PesertaInfoRequest>> getPesertaInfo(String token, Integer idPeserta) {
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

        // Fetch peserta details by ID Peserta
        List<PesertaInfoRequest> pesertaOptional = pesertaRepository.findPesertaInfoByIdPeserta(idPeserta);

        if (pesertaOptional.isEmpty()) {
            return ResponseEntity.status(200).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        PesertaInfoRequest peserta = pesertaOptional.get(0);

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                peserta
        ));
    }

    public ResponseEntity<ResponseWrapper<List<Object[]>>> getPesertaLowonganNotRekrutmen(String token, Integer idPeserta) {
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

        // Fetch data from the repository
        List<Object[]> results = pesertaRepository.findPesertaLowonganByIdPesertaAndIsRekrutmenFalse(idPeserta);

        if (results.isEmpty()) {
            return ResponseEntity.status(200).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                results
        ));
    }


    public ResponseEntity<ResponseWrapper<Object[]>> getPesertaData(String token, Integer idPeserta) {
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

        // Extract id_peserta from token
        Integer idPesertaFromToken = tokenUtil.extractPesertaId(token);

        // Validate Peserta ID
        if (idPesertaFromToken == null || !idPesertaFromToken.equals(idPeserta)) {
            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("403"),
                    "Unauthorized access. Peserta ID does not match the token.",
                    null
            ));
        }

        // Fetch peserta details by ID Peserta
        Optional<Object[]> pesertaOptional = pesertaRepository.findPesertaDataByIdPesertaRaw(idPeserta);

        if (pesertaOptional.isEmpty()) {
            return ResponseEntity.status(200).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        Object[] peserta = pesertaOptional.get();

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                peserta
        ));
    }

    public Map<String, Object> getPesertaDetails(Integer idPeserta) {
        List<Object[]> results = pesertaRepository.findPesertaDetails(idPeserta);

        Map<String, Object> response = new HashMap<>();
        if (!results.isEmpty()) {
            Object[] record = results.get(0);
            response.put("peserta_id", record[0]);
            response.put("profile_picture", record[1]);
            response.put("pendidikan_id", record[7]);
            response.put("pendidikan_jenjang", record[8]);
            response.put("nama_institusi", record[9]);
            response.put("jurusan", record[10]);
            response.put("thn_masuk", record[11]);
            response.put("thn_lulus", record[12]);
            response.put("nilai", record[13]);
            response.put("gelar", record[14]);
            response.put("kontak_id", record[21]);
            response.put("nama_kontak", record[22]);
            response.put("hub_kontak", record[23]);
            response.put("telp_kontak", record[24]);
            response.put("email_kontak", record[25]);
            response.put("alamat_kontak", record[26]);
        }

        return response;
    }


    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> updateProfilePicture(Integer idPeserta, String base64Image) {
        Optional<Peserta> pesertaOptional = pesertaRepository.findByIdPeserta(idPeserta);

        if (pesertaOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Peserta not found",
                    null
            ));
        }

        // Update the profile picture
        pesertaRepository.updateProfilePicture(idPeserta, base64Image);

        // Return the request body in the response's data field
        Map<String, String> responseData = new HashMap<>();
        responseData.put("base64_image", base64Image);

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Profile picture updated successfully",
                responseData
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> updateIsFinal(Integer idPeserta) {
        // Fetch the peserta by ID
        Optional<Peserta> pesertaOptional = pesertaRepository.findByIdPeserta(idPeserta);

        if (pesertaOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Peserta not found",
                    null
            ));
        }

        Peserta peserta = pesertaOptional.get();

        // Check if is_final is already true
        if (Boolean.TRUE.equals(peserta.getIsFinal())) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Peserta is_final is already true",
                    null
            ));
        }

        // Update the is_final field
        peserta.setIsFinal(true);
        peserta.setUpdatedAt(LocalDateTime.now());
        pesertaRepository.save(peserta);

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Peserta is_final updated successfully",
                null
        ));
    }

    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getTinggiBerat(String token, Integer idPeserta) {
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

        Optional<Peserta> pesertaOptional = pesertaRepository.findByIdPeserta(idPeserta);

        Map<String, Object> response = new HashMap<>();
        if (pesertaOptional.isEmpty()) {
            response.put("tinggi", null);
            response.put("berat", null);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    response
            ));
        }

        Peserta peserta = pesertaOptional.get();
        response.put("tinggi", peserta.getTinggi());
        response.put("berat", peserta.getBerat());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                response
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> updateTinggiBerat(String token, Integer idPeserta, Integer tinggi, Integer berat) {
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

        Optional<Peserta> pesertaOptional = pesertaRepository.findByIdPeserta(idPeserta);

        if (pesertaOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Peserta not found",
                    null
            ));
        }

        Peserta peserta = pesertaOptional.get();

        if (tinggi != null) {
            peserta.setTinggi(tinggi);
        }
        if (berat != null) {
            peserta.setBerat(berat);
        }

        peserta.setUpdatedAt(LocalDateTime.now());
        pesertaRepository.save(peserta);

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Tinggi and Berat updated successfully",
                null
        ));
    }

    @Transactional
    public boolean setAgeLimit(Integer idPeserta) {
        Optional<Peserta> pesertaOptional = pesertaRepository.findByIdPeserta(idPeserta);
        if (pesertaOptional.isPresent()) {
            pesertaRepository.updateAgeLimit(idPeserta);
            return true;
        }
        return false;
    }

    public ResponseEntity<ResponseWrapper<Map<String, Object>>> checkAgeLimit(Integer idPeserta) {
        Optional<Peserta> pesertaOptional = pesertaRepository.findByIdPeserta(idPeserta);

        Map<String, Object> response = new HashMap<>();
        if (pesertaOptional.isEmpty()) {
            response.put("age_limit", null);
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Peserta not found",
                    response
            ));
        }

        Peserta peserta = pesertaOptional.get();
        response.put("age_limit", peserta.getAgeLimit());

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                response
        ));
    }


}

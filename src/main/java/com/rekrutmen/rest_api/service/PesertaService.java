package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.PesertaInfoRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.dto.UpdateProfilePictureRequest;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.model.PesertaLowongan;
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

    public List<Peserta> getAllUsers() {
        return pesertaRepository.findAll();
    }

    public void updateUser(Peserta peserta) {
        pesertaRepository.save(peserta);
    }

    public Optional<Peserta> getUserByEmail(String email) {
        return pesertaRepository.findByEmail(email);
    }

    public Optional<Peserta> getProfileByIdPeserta(Integer idPeserta) {
        return pesertaRepository.findByIdPeserta(idPeserta);
    }

    public boolean isUsernameTaken(String username) {
        return pesertaRepository.existsByUsername(username);
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
        return pesertaRepository.save(peserta);  // Use JpaRepository's save method
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
        Optional<PesertaInfoRequest> pesertaOptional = pesertaRepository.findPesertaInfoByIdPeserta(idPeserta);

        if (pesertaOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("077"),
                    responseCodeUtil.getMessage("077"),
                    null
            ));
        }

        PesertaInfoRequest peserta = pesertaOptional.get();

        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                peserta
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

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> updateProfilePicture(Integer idPeserta, String base64Image) {
        Optional<Peserta> pesertaOptional = pesertaRepository.findByIdPeserta(idPeserta);

        if (pesertaOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    "077",
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
                "000",
                "Profile picture updated successfully",
                responseData
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> updateIsFinal(Integer idPeserta) {
        // Fetch the peserta by ID
        Optional<Peserta> pesertaOptional = pesertaRepository.findByIdPeserta(idPeserta);

        if (pesertaOptional.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    "404",
                    "Peserta not found",
                    null
            ));
        }

        Peserta peserta = pesertaOptional.get();

        // Check if is_final is already true
        if (Boolean.TRUE.equals(peserta.getIsFinal())) {
            return ResponseEntity.status(400).body(new ResponseWrapper<>(
                    "400",
                    "Peserta is_final is already true",
                    null
            ));
        }

        // Update the is_final field
        peserta.setIsFinal(true);
        peserta.setUpdatedAt(LocalDateTime.now());
        pesertaRepository.save(peserta);

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Peserta is_final updated successfully",
                null
        ));
    }

}

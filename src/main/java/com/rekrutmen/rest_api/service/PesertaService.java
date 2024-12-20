package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.repository.PesertaRepository;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public boolean isTelpTaken(String telp) {
        return pesertaRepository.existsByTelp(telp);
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

}

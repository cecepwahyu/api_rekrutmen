package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.repository.PesertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PesertaService {

    private final PesertaRepository pesertaRepository;

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

    public boolean isUsernameTaken(String username) {
        return pesertaRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return pesertaRepository.existsByEmail(email);
    }

    public Peserta registerUser(Peserta user) {
        return pesertaRepository.save(user);
    }

    public boolean isNoIdentitasExist(String noIdentitas) {
        return pesertaRepository.existsByNoIdentitas(noIdentitas);
    }

}

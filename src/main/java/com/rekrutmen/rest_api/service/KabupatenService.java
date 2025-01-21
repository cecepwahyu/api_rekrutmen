package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.Kabupaten;
import com.rekrutmen.rest_api.repository.KabupatenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KabupatenService {

    @Autowired
    private KabupatenRepository kabupatenRepository;

    // Fetch all kabupaten
    public List<Kabupaten> getAllKabupaten() {
        return kabupatenRepository.findAll();
    }

    // Fetch kabupaten by kodeProvinsi
    public List<Kabupaten> getKabupatenByProvinsi(String kodeProvinsi) {
        return kabupatenRepository.findByKodeProvinsi(kodeProvinsi);
    }
}

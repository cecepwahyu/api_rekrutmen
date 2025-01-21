package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.Kecamatan;
import com.rekrutmen.rest_api.repository.KecamatanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KecamatanService {

    @Autowired
    private KecamatanRepository kecamatanRepository;

    // Fetch all kecamatan
    public List<Kecamatan> getAllKecamatan() {
        return kecamatanRepository.findAll();
    }

    // Fetch kecamatan by kodeKabupaten
    public List<Kecamatan> getKecamatanByKabupaten(String kodeKabupaten) {
        return kecamatanRepository.findByKodeKabupaten(kodeKabupaten);
    }
}

package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.Desa;
import com.rekrutmen.rest_api.repository.DesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesaService {

    @Autowired
    private DesaRepository desaRepository;

    // Fetch all desa
    public List<Desa> getAllDesa() {
        return desaRepository.findAll();
    }

    // Fetch desa by kodeKecamatan
    public List<Desa> getDesaByKecamatan(String kodeKecamatan) {
        return desaRepository.findByKodeKecamatan(kodeKecamatan);
    }
}

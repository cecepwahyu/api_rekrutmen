package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.Provinsi;
import com.rekrutmen.rest_api.repository.ProvinsiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinsiService {

    @Autowired
    private ProvinsiRepository provinsiRepository;

    public List<Provinsi> getAllProvinsi() {
        return provinsiRepository.findAll();
    }
}

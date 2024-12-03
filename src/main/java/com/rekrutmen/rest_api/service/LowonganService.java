package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.Lowongan;
import com.rekrutmen.rest_api.repository.LowonganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LowonganService {

    @Autowired
    private LowonganRepository lowonganRepository;

    public List<Lowongan> getAllLowongans() {
        return lowonganRepository.findAll();
    }
}

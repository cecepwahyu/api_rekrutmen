package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.Artikel;
import com.rekrutmen.rest_api.model.PengumumanUmum;
import com.rekrutmen.rest_api.repository.ArtikelRepository;
import com.rekrutmen.rest_api.repository.PengumumanUmumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PengumumanUmumService {

    @Autowired
    private PengumumanUmumRepository pengumumanUmumRepository;

    public List<PengumumanUmum> getAllPengumumanUmums() {
        return pengumumanUmumRepository.findAll();
    }
}

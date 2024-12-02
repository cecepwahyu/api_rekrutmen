package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.*;
import com.rekrutmen.rest_api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private PesertaRepository pesertaRepository;

    @Autowired
    private PesertaKontakRepository pesertaKontakRepository;

    @Autowired
    private PesertaPendidikanRepository pesertaPendidikanRepository;

    @Autowired
    private PesertaPengalamanRepository pesertaPengalamanRepository;

    @Autowired
    private PesertaOrganisasiRepository pesertaOrganisasiRepository;

    public void createProfile(Peserta peserta) {
        pesertaRepository.save(peserta);
    }

//    public boolean isNikExist(String noIdentitas) {
//        return pesertaRepository.existsByNik(noIdentitas);
//    }

    public boolean isNoIdentitasExist(String noIdentitas) {
        return pesertaRepository.existsByNoIdentitas(noIdentitas);
    }

    public Optional<Peserta> validateEmailAndNoIdentitas(String email, String noIdentitas) {
        return pesertaRepository.findByEmailAndNoIdentitas(email, noIdentitas);
    }


    public Peserta updateProfile(Peserta peserta) {
        return pesertaRepository.save(peserta);
    }

    @Transactional
    public void updateKerabat(Integer idPeserta, List<PesertaKontak> family) {
        // Remove existing family records
        pesertaKontakRepository.deleteByIdPeserta(idPeserta);

        // Insert new family records
        family.forEach(kerabat -> {
            kerabat.setIdPeserta(idPeserta);
            pesertaKontakRepository.save(kerabat);
        });
    }

    @Transactional
    public void updatePendidikan(Integer idPeserta, List<PesertaPendidikan> education) {
        // Remove existing education records
        pesertaPendidikanRepository.deleteByIdPeserta(idPeserta); // Corrected method name

        // Insert new education records
        education.forEach(pendidikan -> {
            pendidikan.setIdPeserta(idPeserta);
            pesertaPendidikanRepository.save(pendidikan);
        });
    }

    @Transactional
    public void updatePengalamanKerja(Integer idPeserta, List<PesertaPengalaman> work) {
        // Remove existing education records
        pesertaPendidikanRepository.deleteByIdPeserta(idPeserta);

        // Insert new education records
        work.forEach(pendidikan -> {
            pendidikan.setIdPeserta(idPeserta);
            pesertaPengalamanRepository.save(pendidikan);
        });
    }

    @Transactional
    public void updateOrganisasi(Integer profileId, List<PesertaOrganisasi> work) {
        // Remove existing education records
        pesertaPendidikanRepository.deleteByIdPeserta(profileId);

        // Insert new education records
        work.forEach(pendidikan -> {
            pendidikan.setIdPeserta(profileId);
            pesertaOrganisasiRepository.save(pendidikan);
        });
    }
}

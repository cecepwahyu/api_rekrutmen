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

    public Optional<Peserta> validateOtp(String otp) {
        return pesertaRepository.findByOtp(otp);
    }

    public void updateOtp(Integer idPeserta, String otpCode) {
        Optional<Peserta> pesertaOptional = pesertaRepository.findById(Long.valueOf(idPeserta));
        if (pesertaOptional.isPresent()) {
            Peserta peserta = pesertaOptional.get();
            peserta.setOtp(otpCode);
            pesertaRepository.save(peserta);
        }
    }

    public Peserta updateProfile(Peserta peserta) {
        return pesertaRepository.save(peserta);
    }

    @Transactional
    public void updateKerabat(Integer idPeserta, List<PesertaKontak> contact) {
        // Remove existing contact records
        pesertaKontakRepository.deleteByIdPeserta(idPeserta);

        // Insert new contact records
        contact.forEach(kontak -> {
            kontak.setIdPeserta(idPeserta);
            pesertaKontakRepository.save(kontak);
        });
    }

    @Transactional
    public void updatePendidikan(Integer idPeserta, List<PesertaPendidikan> education) {
        // Remove existing education records
        pesertaPendidikanRepository.deleteByIdPeserta(idPeserta);

        // Insert new education records
        education.forEach(pendidikan -> {
            pendidikan.setIdPeserta(idPeserta);
            pesertaPendidikanRepository.save(pendidikan);
        });
    }

    @Transactional
    public void updatePengalamanKerja(Integer idPeserta, List<PesertaPengalaman> work) {
        // Remove existing work records
        pesertaPengalamanRepository.deleteByIdPeserta(idPeserta);

        // Insert new work records
        work.forEach(pekerjaan -> {
            pekerjaan.setIdPeserta(idPeserta);
            pesertaPengalamanRepository.save(pekerjaan);
        });
    }

    @Transactional
    public void updateOrganisasi(Integer profileId, List<PesertaOrganisasi> work) {
        // Remove existing organization records
        pesertaOrganisasiRepository.deleteByIdPeserta(profileId);

        // Insert new organization records
        work.forEach(organisasi -> {
            organisasi.setIdPeserta(profileId);
            pesertaOrganisasiRepository.save(organisasi);
        });
    }
}

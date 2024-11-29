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
    private ProfileRepository profileRepository;

    @Autowired
    private KerabatRepository kerabatRepository;

    @Autowired
    private RiwayatPendidikanRepository riwayatPendidikanRepository;

    @Autowired
    private PengalamanKerjaRepository pengalamanKerjaRepository;

    @Autowired
    private RiwayatOrganisasiRepository riwayatOrganisasiRepository;

    public void createProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public boolean isNikExist(String nik) {
        return profileRepository.existsByNik(nik);
    }

    public Optional<Profile> validateEmailAndNik(String email, String nik) {
        return profileRepository.findByEmailAndNik(email, nik);
    }


    public Profile updateProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    @Transactional
    public void updateKerabat(Integer profileId, List<Kerabat> family) {
        // Remove existing family records
        kerabatRepository.deleteByProfileId(profileId);

        // Insert new family records
        family.forEach(kerabat -> {
            kerabat.setProfileId(profileId);
            kerabatRepository.save(kerabat);
        });
    }

    @Transactional
    public void updatePendidikan(Integer profileId, List<RiwayatPendidikan> education) {
        // Remove existing education records
        riwayatPendidikanRepository.deleteByProfileId(profileId);

        // Insert new education records
        education.forEach(pendidikan -> {
            pendidikan.setProfileId(profileId);
            riwayatPendidikanRepository.save(pendidikan);
        });
    }

    @Transactional
    public void updatePengalamanKerja(Integer profileId, List<PengalamanKerja> work) {
        // Remove existing education records
        riwayatPendidikanRepository.deleteByProfileId(profileId);

        // Insert new education records
        work.forEach(pendidikan -> {
            pendidikan.setProfileId(profileId);
            pengalamanKerjaRepository.save(pendidikan);
        });
    }

    @Transactional
    public void updateOrganisasi(Integer profileId, List<RiwayatOrganisasi> work) {
        // Remove existing education records
        riwayatPendidikanRepository.deleteByProfileId(profileId);

        // Insert new education records
        work.forEach(pendidikan -> {
            pendidikan.setProfileId(profileId);
            riwayatOrganisasiRepository.save(pendidikan);
        });
    }
}

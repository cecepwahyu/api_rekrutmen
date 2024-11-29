package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.EditProfileRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.*;
import com.rekrutmen.rest_api.service.ProfileService;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @PutMapping("/{profileId}/edit")
    public ResponseEntity<ResponseWrapper<Object>> editProfile(
            @PathVariable Integer profileId,
            @RequestBody EditProfileRequest request
    ) {
        // Update profile data
        Profile profile = new Profile();
        profile.setProfileId(profileId);
        profile.setFullName(request.getFullName());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setEmail(request.getEmail());
        profile.setAddress(request.getAddress());
        //profile.setProfilePicture(request.getProfilePicture());
        profileService.updateProfile(profile);

        // Update family data
        List<Kerabat> family = request.getKerabat().stream().map(f -> {
            Kerabat kerabat = new Kerabat();
            kerabat.setName(f.getName());
            kerabat.setRelationship(f.getRelationship());
            kerabat.setPhoneNumber(f.getPhoneNumber());
            kerabat.setEmail(f.getEmail());
            kerabat.setAddress(f.getAddress());
            return kerabat;
        }).collect(Collectors.toList());
        profileService.updateKerabat(profileId, family);

        // Update riwayat pendidikan data
        List<RiwayatPendidikan> education = request.getRiwayatPendidikan().stream().map(e -> {
            RiwayatPendidikan pendidikan = new RiwayatPendidikan();
            pendidikan.setInstitutionName(e.getInstitutionName());
            pendidikan.setDegree(e.getDegree());
            pendidikan.setFieldOfStudy(e.getFieldOfStudy());
            pendidikan.setStartDate(e.getStartDate());
            pendidikan.setEndDate(e.getEndDate());
            pendidikan.setAchievements(e.getAchievements());
            return pendidikan;
        }).collect(Collectors.toList());
        profileService.updatePendidikan(profileId, education);

        // Update riwayat organisasi data
        List<RiwayatOrganisasi> organization = request.getRiwayatOrganisasi().stream().map(e -> {
            RiwayatOrganisasi organisasi = new RiwayatOrganisasi();
            organisasi.setOrganizationName(e.getOrganizationName());
            organisasi.setRole(e.getRole());
            organisasi.setStartDate(e.getStartDate());
            organisasi.setEndDate(e.getEndDate());
            organisasi.setResponsibilities(e.getResponsibilities());
            return organisasi;
        }).collect(Collectors.toList());
        profileService.updateOrganisasi(profileId, organization);

        //Update riwayat pekerjaan data
        List<PengalamanKerja> work = request.getPengalamanKerja().stream().map(e -> {
            PengalamanKerja pekerjaan = new PengalamanKerja();
            pekerjaan.setCompanyName(e.getCompanyName());
            pekerjaan.setPosition(e.getPosition());
            pekerjaan.setStartDate(e.getStartDate());
            pekerjaan.setEndDate(e.getEndDate());
            pekerjaan.setResponsibilities(e.getResponsibilities());
            return pekerjaan;
        }).collect(Collectors.toList());
        profileService.updatePengalamanKerja(profileId, work);

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                responseCodeUtil.getMessage("000"),
                "Profile updated successfully"
        ));
    }
}

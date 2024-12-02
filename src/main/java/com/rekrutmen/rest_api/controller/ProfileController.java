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
            @PathVariable Integer idPeserta,
            @RequestBody EditProfileRequest request
    ) {
        // Update profile data
        Peserta peserta = new Peserta();
        peserta.setIdPeserta(idPeserta);
        peserta.setNama(request.getNama());
        peserta.setNoIdentitas(request.getNoIdentitas());
        peserta.setTempatLahir(request.getTempatLahir());
        peserta.setTglLahir(request.getTglLahir());
        peserta.setJnsKelamin(request.getJnsKelamin());
        peserta.setAgama(request.getAgama());
        peserta.setAlamatIdentitas(request.getAlamatIdentitas());
        peserta.setProvinsiIdentitas(request.getProvinsiIdentitas());
        peserta.setKotaIdentitas(request.getKotaIdentitas());
        peserta.setKecamatanIdentitas(request.getKecamatanIdentitas());
        peserta.setDesaIdentitas(request.getDesaIdentitas());
        peserta.setTelp(request.getTelp());
        peserta.setEmail(request.getEmail());
        peserta.setPendidikanTerakhir(request.getPendidikanTerakhir());
        peserta.setStatusKawin(request.getStatusKawin());
        peserta.setIdSession(request.getIdSession());
        peserta.setIpLogin(request.getIpLogin());
        peserta.setFlgStatus(request.getFlgStatus());
        peserta.setTglStatus(request.getTglStatus());
        peserta.setUpdatedAt(request.getUpdatedAt());
        profileService.updateProfile(peserta);

        // Update kontak data
        List<PesertaKontak> contact = request.getKontak().stream().map(f -> {
            PesertaKontak kontak = new PesertaKontak();
            kontak.setNamaKontak(f.getNamaKontak());
            kontak.setHubKontak(f.getHubKontak());
            kontak.setTelpKontak(f.getTelpKontak());
            kontak.setEmailKontak(f.getEmailKontak());
            kontak.setAlamatKontak(f.getAlamatKontak());
            return kontak;
        }).collect(Collectors.toList());
        profileService.updateKerabat(idPeserta, contact);

        // Update riwayat pendidikan data
        List<PesertaPendidikan> education = request.getPesertaPendidikan().stream().map(e -> {
            PesertaPendidikan pendidikan = new PesertaPendidikan();
            pendidikan.setIdJenjang(e.getIdJenjang());
            pendidikan.setNamaInstitusi(e.getNamaInstitusi());
            pendidikan.setJurusan(e.getJurusan());
            pendidikan.setThnMasuk(e.getThnMasuk());
            pendidikan.setThnLulus(e.getThnLulus());
            pendidikan.setNilai(e.getNilai());
            pendidikan.setGelar(e.getGelar());
            pendidikan.setAchievements(e.getAchievements());
            return pendidikan;
        }).collect(Collectors.toList());
        profileService.updatePendidikan(idPeserta, education);

        // Update riwayat organisasi data
        List<PesertaOrganisasi> organization = request.getPesertaOrganisasi().stream().map(e -> {
            PesertaOrganisasi organisasi = new PesertaOrganisasi();
            organisasi.setNamaOrganisasi(e.getNamaOrganisasi());
            organisasi.setPosisiOrganisasi(e.getPosisiOrganisasi());
            organisasi.setPeriode(e.getPeriode());
            organisasi.setDeskripsiKerja(e.getDeskripsiKerja());
            return organisasi;
        }).collect(Collectors.toList());
        profileService.updateOrganisasi(idPeserta, organization);

        //Update riwayat pekerjaan data
        List<PesertaPengalaman> work = request.getPesertaPengalaman().stream().map(e -> {
            PesertaPengalaman pekerjaan = new PesertaPengalaman();
            pekerjaan.setNamaInstansi(e.getNamaInstansi());
            pekerjaan.setPosisiKerja(e.getPosisiKerja());
            pekerjaan.setPeriodeKerja(e.getPeriodeKerja());
            pekerjaan.setDeskripsiKerja(e.getDeskripsiKerja());
            return pekerjaan;
        }).collect(Collectors.toList());
        profileService.updatePengalamanKerja(idPeserta, work);

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                responseCodeUtil.getMessage("000"),
                "Profile updated successfully"
        ));
    }
}

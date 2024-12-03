package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.EditProfileRequest;
import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.*;
import com.rekrutmen.rest_api.service.PesertaService;
import com.rekrutmen.rest_api.service.ProfileService;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;
    @Autowired
    private PesertaService pesertaService;

    @PutMapping("/{idPeserta}/edit")
    public ResponseEntity<ResponseWrapper<Object>> editProfile(
            @PathVariable Integer idPeserta,
            @RequestBody EditProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        // Retrieve the existing profile
        Optional<Peserta> optionalPeserta = pesertaService.getProfileByIdPeserta(idPeserta);
        if (optionalPeserta.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    "404",
                    responseCodeUtil.getMessage("404"),
                    "Profile not found"
            ));
        }

        //Checking duplicate email
        if (pesertaService.isEmailTaken(request.getEmail())) {
            logger.warn("Email: {} already exist!", request.getEmail());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "400",
                    responseCodeUtil.getMessage("400"),
                    "Email already exist"
            ));
        }

        //Checking duplicate telp number
        if (pesertaService.isTelpTaken(request.getTelp())) {
            logger.warn("Telp: {} already exist!", request.getTelp());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "400",
                    responseCodeUtil.getMessage("400"),
                    "Telp already exist"
            ));
        }

        // Get the Peserta object from Optional
        Peserta existingPeserta = optionalPeserta.get();

        //Get IP Address user
        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        // Update profile data
        existingPeserta.setNama(request.getNama() != null ? request.getNama() : existingPeserta.getNama());
        existingPeserta.setNoIdentitas(request.getNoIdentitas() != null ? request.getNoIdentitas() : existingPeserta.getNoIdentitas());
        existingPeserta.setTempatLahir(request.getTempatLahir() != null ? request.getTempatLahir() : existingPeserta.getTempatLahir());
        existingPeserta.setTglLahir(request.getTglLahir() != null ? request.getTglLahir() : existingPeserta.getTglLahir());
        existingPeserta.setJnsKelamin(request.getJnsKelamin() != null ? request.getJnsKelamin() : existingPeserta.getJnsKelamin());
        existingPeserta.setAgama(request.getAgama() != null ? request.getAgama() : existingPeserta.getAgama());
        existingPeserta.setAlamatIdentitas(request.getAlamatIdentitas() != null ? request.getAlamatIdentitas() : existingPeserta.getAlamatIdentitas());
        existingPeserta.setProvinsiIdentitas(request.getProvinsiIdentitas() != null ? request.getProvinsiIdentitas() : existingPeserta.getProvinsiIdentitas());
        existingPeserta.setKotaIdentitas(request.getKotaIdentitas() != null ? request.getKotaIdentitas() : existingPeserta.getKotaIdentitas());
        existingPeserta.setKecamatanIdentitas(request.getKecamatanIdentitas() != null ? request.getKecamatanIdentitas() : existingPeserta.getKecamatanIdentitas());
        existingPeserta.setDesaIdentitas(request.getDesaIdentitas() != null ? request.getDesaIdentitas() : existingPeserta.getDesaIdentitas());
        existingPeserta.setAlamatDomisili(request.getAlamatDomisili() != null ? request.getAlamatDomisili() : existingPeserta.getAlamatDomisili());
        existingPeserta.setProvinsiDomisili(request.getProvinsiDomisili() != null ? request.getProvinsiDomisili() : existingPeserta.getProvinsiDomisili());
        existingPeserta.setKotaDomisili(request.getKotaDomisili() != null ? request.getKotaDomisili() : existingPeserta.getKotaDomisili());
        existingPeserta.setKecamatanDomisili(request.getKecamatanDomisili() != null ? request.getKecamatanDomisili() : existingPeserta.getKecamatanDomisili());
        existingPeserta.setDesaDomisili(request.getDesaDomisili() != null ? request.getDesaDomisili() : existingPeserta.getDesaDomisili());
        existingPeserta.setTelp(request.getTelp() != null ? request.getTelp() : existingPeserta.getTelp());
        existingPeserta.setEmail(request.getEmail() != null ? request.getEmail() : existingPeserta.getEmail());
        existingPeserta.setPendidikanTerakhir(request.getPendidikanTerakhir() != null ? request.getPendidikanTerakhir() : existingPeserta.getPendidikanTerakhir());
        existingPeserta.setStatusKawin(request.getStatusKawin() != null ? request.getStatusKawin() : existingPeserta.getStatusKawin());
        existingPeserta.setIdSession(request.getIdSession() != null ? request.getIdSession() : existingPeserta.getIdSession());
        existingPeserta.setIpLogin(ipAddress);
        existingPeserta.setFlgStatus(request.getFlgStatus() != null ? request.getFlgStatus() : existingPeserta.getFlgStatus());
        existingPeserta.setTglStatus(request.getTglStatus() != null ? request.getTglStatus() : existingPeserta.getTglStatus());
        existingPeserta.setUpdatedAt(LocalDateTime.now());

        profileService.updateProfile(existingPeserta);

        // Update kontak data
        if (request.getKontak() != null) {
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
        }

        // Update riwayat pendidikan data
        if (request.getPesertaPendidikan() != null) {
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
        }

        // Update riwayat organisasi data
        if (request.getPesertaOrganisasi() != null) {
            List<PesertaOrganisasi> organization = request.getPesertaOrganisasi().stream().map(e -> {
                PesertaOrganisasi organisasi = new PesertaOrganisasi();
                organisasi.setNamaOrganisasi(e.getNamaOrganisasi());
                organisasi.setPosisiOrganisasi(e.getPosisiOrganisasi());
                organisasi.setPeriode(e.getPeriode());
                organisasi.setDeskripsiKerja(e.getDeskripsiKerja());
                return organisasi;
            }).collect(Collectors.toList());
            profileService.updateOrganisasi(idPeserta, organization);
        }

        // Update riwayat pekerjaan data
        if (request.getPesertaPengalaman() != null) {
            List<PesertaPengalaman> work = request.getPesertaPengalaman().stream().map(e -> {
                PesertaPengalaman pekerjaan = new PesertaPengalaman();
                pekerjaan.setNamaInstansi(e.getNamaInstansi());
                pekerjaan.setPosisiKerja(e.getPosisiKerja());
                pekerjaan.setPeriodeKerja(e.getPeriodeKerja());
                pekerjaan.setDeskripsiKerja(e.getDeskripsiKerja());
                return pekerjaan;
            }).collect(Collectors.toList());
            profileService.updatePengalamanKerja(idPeserta, work);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                responseCodeUtil.getMessage("000"),
                "Profile updated successfully"
        ));
    }
}

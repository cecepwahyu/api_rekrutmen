package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.*;
import com.rekrutmen.rest_api.model.*;
import com.rekrutmen.rest_api.service.*;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
public class PesertaController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    @Autowired
    private PesertaService pesertaService;

    @Autowired
    private PesertaPengalamanService pesertaPengalamanService;

    @Autowired
    private PesertaPendidikanService pesertaPendidikanService;

    @Autowired
    private PesertaOrganisasiService pesertaOrganisasiService;

    @Autowired
    private PesertaKontakService pesertaKontakService;

    @Autowired
    private PesertaDocumentsService pesertaDocumentsService;

    @GetMapping("/{idPeserta}")
    public ResponseEntity<ResponseWrapper<Peserta>> getPesertaDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta) {
        return pesertaService.getPesertaDetail(token, idPeserta);
    }

    @GetMapping("/pengalaman/{idPeserta}")
    public ResponseEntity<ResponseWrapper<PesertaPengalaman>> getPesertaPengalamanDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta) {
        return pesertaPengalamanService.getPesertaPengalamanDetail(token, idPeserta);
    }

    @GetMapping("/pendidikan/{idPeserta}")
    public ResponseEntity<ResponseWrapper<PesertaPendidikan>> getPesertaPendidikanDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta) {
        return pesertaPendidikanService.getPesertaPendidikanDetail(token, idPeserta);
    }

    @GetMapping("/organisasi/{idPeserta}")
    public ResponseEntity<ResponseWrapper<PesertaOrganisasi>> getPesertaOrganisasiDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta) {
        return pesertaOrganisasiService.getPesertaOrganisasiDetail(token, idPeserta);
    }

    @GetMapping("/kontak/{idPeserta}")
    public ResponseEntity<ResponseWrapper<PesertaKontak>> getPesertaKontakDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta) {
        return pesertaKontakService.getPesertaKontakDetail(token, idPeserta);
    }

    @GetMapping("/peserta-info/{idPeserta}")
    public ResponseEntity<?> getPesertaInfo(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta
    ) {
        ResponseEntity<ResponseWrapper<PesertaInfoRequest>> response = pesertaService.getPesertaInfo(token, idPeserta);

        if (response.getBody() != null && response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }

    @PutMapping("/{idPeserta}/update-profile-picture")
    public ResponseEntity<ResponseWrapper<Object>> updateProfilePicture(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid UpdateProfilePictureRequest request
    ) {
        return pesertaService.updateProfilePicture(idPeserta, request.getBase64Image());
    }

    @PutMapping("/{idPeserta}/edit")
    public ResponseEntity<ResponseWrapper<Object>> editProfile(
            @PathVariable Integer idPeserta,
            @RequestBody EditProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        // Retrieve the existing profile
        Optional<Peserta> optionalPeserta = pesertaService.getProfileByIdPeserta(idPeserta);
        if (optionalPeserta.isEmpty()) {
         }

        // Checking duplicate telp number
        logger.info("Checking telp duplication for idPeserta: {}, telp: {}", idPeserta, request.getTelp());
        if (pesertaService.isTelpTaken(request.getTelp(), idPeserta)) {
            logger.warn("Telp: {} already exists!", request.getTelp());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    "400",
                    responseCodeUtil.getMessage("400"),
                    "Telp already exists"
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

    @GetMapping("/{idPeserta}/documents")
    public ResponseEntity<List<Integer>> getIdDocumentsByUserId(@PathVariable Integer idPeserta) {
        List<Integer> idDocuments = pesertaDocumentsService.getIdDocumentsByUserId(idPeserta);
        return ResponseEntity.ok(idDocuments);
    }

    @PutMapping("/{idPeserta}/submit-ktp")
    public ResponseEntity<ResponseWrapper<Object>> submitKtp(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateKtp(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-skck")
    public ResponseEntity<ResponseWrapper<Object>> submitSkck(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateSkck(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-toefl")
    public ResponseEntity<ResponseWrapper<Object>> submitToefl(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateToefl(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-kk")
    public ResponseEntity<ResponseWrapper<Object>> submitKartuKeluarga(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateKartuKeluarga(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-suratsehat")
    public ResponseEntity<ResponseWrapper<Object>> submitSuratSehat(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateSuratSehat(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-cv")
    public ResponseEntity<ResponseWrapper<Object>> submitCv(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateCv(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-suratlamaran")
    public ResponseEntity<ResponseWrapper<Object>> submitSuratLamaran(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateSuratLamaran(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-suratpernyataan")
    public ResponseEntity<ResponseWrapper<Object>> submitSuratPernyataan(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateSuratPernyataan(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-ijazah")
    public ResponseEntity<ResponseWrapper<Object>> submitIjazah(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateIjazah(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-transkrip")
    public ResponseEntity<ResponseWrapper<Object>> submitTranskrip(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateTranskrip(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-fotofullbadan")
    public ResponseEntity<ResponseWrapper<Object>> submitFotoFullBadan(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdateFotoFullBadan(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }

    @PutMapping("/{idPeserta}/submit-pasfoto")
    public ResponseEntity<ResponseWrapper<Object>> submitPasFoto(
            @PathVariable Integer idPeserta,
            @RequestBody @Valid SubmitDocumentRequest request) {

        return pesertaDocumentsService.submitOrUpdatePasFoto(
                idPeserta,
                request.getDocumentData(),
                request.getFileName(),
                request.getFileType()
        );
    }
}

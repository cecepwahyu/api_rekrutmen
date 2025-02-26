package com.rekrutmen.rest_api.controller;

import com.rekrutmen.rest_api.dto.*;
import com.rekrutmen.rest_api.model.*;
import com.rekrutmen.rest_api.service.*;
import com.rekrutmen.rest_api.util.LoggerUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    private PesertaLowonganService pesertaLowonganService;

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

    @Autowired
    private TokenUtil tokenUtil;

    @GetMapping("/{idPeserta}")
    public ResponseEntity<ResponseWrapper<Peserta>> getPesertaDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "GET_PESERTA_DETAIL";
        return pesertaService.getPesertaDetail(processName, token, idPeserta, request);
    }

    @GetMapping("/pengalaman/{idPeserta}")
    public ResponseEntity<ResponseWrapper<List<PesertaPengalaman>>> getPesertaPengalamanDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "GET_PESERTA_PENGALAMAN";
        return pesertaPengalamanService.getPesertaPengalamanDetail(processName, token, idPeserta, request);
    }

    @GetMapping("/pendidikan/{idPeserta}")
    public ResponseEntity<ResponseWrapper<List<PesertaPendidikan>>> getPesertaPendidikanDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "GET_PESERTA_PENDIDIKAN";
        return pesertaPendidikanService.getPesertaPendidikanDetail(processName, token, idPeserta, request);
    }

    @GetMapping("/organisasi/{idPeserta}")
    public ResponseEntity<ResponseWrapper<List<PesertaOrganisasi>>> getPesertaOrganisasiDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "GET_PESERTA_ORGANISASI";
        return pesertaOrganisasiService.getPesertaOrganisasiDetail(processName, token, idPeserta, request);
    }

    @GetMapping("/kontak/{idPeserta}")
    public ResponseEntity<ResponseWrapper<List<PesertaKontak>>> getPesertaKontakDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "GET_PESERTA_KONTAK";
        return pesertaKontakService.getPesertaKontakDetail(processName, token, idPeserta, request);
    }

    @GetMapping("/{idPeserta}/details")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getPesertaDetails(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "GET_PESERTA_DETAIL";
        try {
            Map<String, Object> pesertaDetails = pesertaService.getPesertaDetails(processName, token, idPeserta, request);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    "000",
                    "Data fetched successfully",
                    pesertaDetails
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }


    @GetMapping("/peserta-info/{idPeserta}")
    public ResponseEntity<?> getPesertaInfo(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request
    ) {
        String processName = "GET_PESERTA_INFO";
        ResponseEntity<ResponseWrapper<PesertaInfoRequest>> response = pesertaService.getPesertaInfo(processName, token, idPeserta, request);

        if (response.getBody() != null && response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }

    @GetMapping("/{idPeserta}/jobdesc")
    public ResponseEntity<ResponseWrapper<List<Object[]>>> getPesertaLowonganNotRekrutmen(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta
    ) {
        return pesertaService.getPesertaLowonganNotRekrutmen(token, idPeserta);
    }


    @GetMapping("/peserta-data/{idPeserta}")
    public ResponseEntity<?> getPesertaData(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta
    ) {
        ResponseEntity<ResponseWrapper<Object[]>> response = pesertaService.getPesertaData(token, idPeserta);

        if (response.getBody() != null && response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }

    @GetMapping("/jobdesc/query")
    public ResponseEntity<ResponseWrapper<List<PesertaLowongan>>> getPesertaLowonganByCriteria(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer idLowongan,
            @RequestParam Integer idPeserta,
            @RequestParam Boolean isRekrutmen) {
        return pesertaLowonganService.getPesertaLowonganByCriteria(token, idLowongan, idPeserta, isRekrutmen);
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
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            @RequestBody EditProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        String processName = "EDIT_PROFILE";
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(httpRequest);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(request)
        ));

        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    Map.of("message", "Invalid token")
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    Map.of("message", "Expired token")
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    null
            ));
        }

        // Extract id_peserta from token
        Integer idPesertaFromToken = tokenUtil.extractPesertaId(token);

        // Validate Peserta ID
        if (idPesertaFromToken == null || !idPesertaFromToken.equals(idPeserta)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("403"),
                    responseCodeUtil.getMessage("403"),
                    Map.of("message", "Unauthorized access. Peserta ID does not match the token.")
            ));
            return ResponseEntity.status(403).body(new ResponseWrapper<>(
                    "403",
                    "Unauthorized access. Peserta ID does not match the token.",
                    null
            ));
        }

        // Retrieve the existing profile
        Optional<Peserta> optionalPeserta = pesertaService.getProfileByIdPeserta(idPeserta);
        if (optionalPeserta.isEmpty()) {
         }

        // Validate input fields
        if (!isValidInput(request.getNama()) || !isValidInput(request.getNoIdentitas()) ||
                !isValidInput(request.getTempatLahir()) || !isValidInput(request.getTelp()) ||
                !isValidInput(String.valueOf(request.getPendidikanTerakhir())) || !isValidInput(String.valueOf(request.getStatusKawin()))) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Invalid input! Only alphabets, numbers, '@', and '.' are allowed.")
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Invalid input! Only alphabets, numbers, '@', and '.' are allowed."
            ));
        }

        // Checking duplicate telp number
        if (pesertaService.isTelpTaken(request.getTelp(), idPeserta)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Telp: " + request.getTelp() + "already exists!")
            ));
            logger.warn("Telp: {} already exists!", request.getTelp());
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    "Nomor Telepon sudah terdaftar"
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
        existingPeserta.setTinggi(request.getTinggi() != null ? request.getTinggi() : existingPeserta.getTinggi());
        existingPeserta.setBerat(request.getBerat() != null ? request.getBerat() : existingPeserta.getBerat());

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
                kontak.setIsBpddiyRelated(f.getIsBpddiyRelated());
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
                organisasi.setSertifikat(e.getSertifikat());
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
                pekerjaan.setSuratPengalamanKerja(e.getSuratPengalamanKerja());
                return pekerjaan;
            }).collect(Collectors.toList());
            profileService.updatePengalamanKerja(idPeserta, work);
        }

        logger.warn(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                responseCodeUtil.getCode("000"),
                responseCodeUtil.getMessage("000"),
                Map.of("message", "Profile updated successfully")
        ));
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

    @PutMapping("/{idPeserta}/submit-document")
    public ResponseEntity<ResponseWrapper<Object>> submitDocument(
            @PathVariable Integer idPeserta,
            @RequestParam Integer idDokumen,
            @RequestBody @Valid SubmitDocumentRequest request
    ) {
        logger.info("Submitting document for idPeserta: {}, idDokumen: {}", idPeserta, idDokumen);

        try {
            // Delegate to service
            ResponseEntity<ResponseWrapper<Object>> response = pesertaDocumentsService.submitOrUpdateDocument(
                    idPeserta,
                    idDokumen,
                    request.getDocumentData(),
                    request.getFileName(),
                    request.getFileType()
            );
            logger.info("Successfully submitted document for idPeserta: {}, idDokumen: {}", idPeserta, idDokumen);
            return response;

        } catch (Exception e) {
            logger.error("Error submitting document for idPeserta: {}, idDokumen: {} - {}", idPeserta, idDokumen, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }

    @PutMapping("/pendidikan/{idPeserta}/insert")
    public ResponseEntity<ResponseWrapper<List<PesertaPendidikan>>> insertPesertaPendidikan(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            @RequestBody @Valid List<PesertaPendidikanRequest> pendidikanRequests) {

        logger.info("Request received to insert PesertaPendidikan for idPeserta: {}", idPeserta);

        try {
            // Delegate to service
            ResponseEntity<ResponseWrapper<List<PesertaPendidikan>>> response =
                    pesertaPendidikanService.insertPesertaPendidikan(token, idPeserta, pendidikanRequests);

            logger.info("Successfully processed PesertaPendidikan for idPeserta: {}", idPeserta);
            return response;

        } catch (Exception e) {
            logger.error("Error inserting PesertaPendidikan for idPeserta: {} - {}", idPeserta, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }

    @PutMapping("/pengalaman/{idPeserta}/insert")
    public ResponseEntity<ResponseWrapper<List<PesertaPengalaman>>> insertPesertaPengalaman(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            @RequestBody @Valid List<PesertaPengalamanRequest> pengalamanRequests) {

        logger.info("Request received to insert PesertaPengalaman for idPeserta: {}", idPeserta);

        try {
            // Delegate to service
            ResponseEntity<ResponseWrapper<List<PesertaPengalaman>>> response =
                    pesertaPengalamanService.insertPesertaPengalaman(token, idPeserta, pengalamanRequests);

            logger.info("Successfully processed PesertaPengalaman for idPeserta: {}", idPeserta);
            return response;

        } catch (Exception e) {
            logger.error("Error inserting PesertaPengalaman for idPeserta: {} - {}", idPeserta, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }

    @PutMapping("/organisasi/{idPeserta}/insert")
    public ResponseEntity<ResponseWrapper<List<PesertaOrganisasi>>> insertPesertaOrganisasi(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            @RequestBody @Valid List<PesertaOrganisasiRequest> organisasiRequests) {

        logger.info("Request received to insert PesertaOrganisasi for idPeserta: {}", idPeserta);

        try {
            // Delegate to service
            ResponseEntity<ResponseWrapper<List<PesertaOrganisasi>>> response =
                    pesertaOrganisasiService.insertPesertaOrganisasi(token, idPeserta, organisasiRequests);

            logger.info("Successfully processed PesertaOrganisasi for idPeserta: {}", idPeserta);
            return response;

        } catch (Exception e) {
            logger.error("Error inserting PesertaOrganisasi for idPeserta: {} - {}", idPeserta, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }

    @PutMapping("/kontak/{idPeserta}/insert")
    public ResponseEntity<ResponseWrapper<List<PesertaKontak>>> insertPesertaKontak(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            @RequestBody @Valid List<PesertaKontakRequest> kontakRequests) {

        logger.info("Request received to insert PesertaKontak for idPeserta: {}", idPeserta);

        try {
            // Delegate to service
            ResponseEntity<ResponseWrapper<List<PesertaKontak>>> response =
                    pesertaKontakService.insertPesertaKontak(token, idPeserta, kontakRequests);

            logger.info("Successfully processed PesertaKontak for idPeserta: {}", idPeserta);
            return response;

        } catch (Exception e) {
            logger.error("Error inserting PesertaKontak for idPeserta: {} - {}", idPeserta, e.getMessage(), e);
            return ResponseEntity.status(500).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("500"),
                    responseCodeUtil.getMessage("500"),
                    null
            ));
        }
    }

    @PutMapping("/{idPeserta}/set-is-final")
    public ResponseEntity<ResponseWrapper<Object>> setIsFinal(
            @PathVariable Integer idPeserta,
            @RequestHeader("Authorization") String token,
            HttpServletRequest request
    ) {
        String processName = "SET_IS_FINAL";
        return pesertaService.updateIsFinal(processName, token, idPeserta, request);
    }

    @GetMapping("/{idPeserta}/tinggi-berat")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getTinggiBerat(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "GET_TINGGI_BERAT_BADAN";
        return pesertaService.getTinggiBerat(processName, token, idPeserta, request);
    }

    @PutMapping("/{idPeserta}/update-tinggi-berat")
    public ResponseEntity<ResponseWrapper<Object>> updateTinggiBerat(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            @RequestBody Map<String, Integer> requestBody,
            HttpServletRequest request) {
        String processName = "UPDATE_TINGGI_BERAT_BADAN";
        Integer tinggi = requestBody.get("tinggi");
        Integer berat = requestBody.get("berat");
        return pesertaService.updateTinggiBerat(processName, token, idPeserta, tinggi, berat, request);
    }

    @PutMapping("/{idPeserta}/set-age-limit")
    public ResponseEntity<ResponseWrapper<Object>> setAgeLimit(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "SET_AGE_LIMIT";
        boolean updated = pesertaService.setAgeLimit(processName, token, idPeserta, request);

        if (updated) {
            return ResponseEntity.ok(new ResponseWrapper<>(
                    "000",
                    "Age limit successfully set to true",
                    null
            ));
        } else {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    "404",
                    "Peserta not found",
                    null
            ));
        }
    }

    @GetMapping("/{idPeserta}/check-age-limit")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> checkAgeLimit(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer idPeserta,
            HttpServletRequest request) {
        String processName = "CHECK_AGE_LIMIT";
        return pesertaService.checkAgeLimit(processName, token, idPeserta, request);
    }

    /**
     * Validates input: Only allows alphabets, numbers, '@', and '.'.
     */
    private boolean isValidInput(String input) {
        if (input == null || input.isEmpty()) return true; // Allow empty fields
        return input.matches("^[a-zA-Z0-9@. ]+$");
    }
}

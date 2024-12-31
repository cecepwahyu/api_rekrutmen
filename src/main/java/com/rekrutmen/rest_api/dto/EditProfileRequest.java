package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Getter
@Setter
public class EditProfileRequest {

    // Profile data
    private String nama;

    @JsonProperty("no_identitas")
    private String noIdentitas;

    @JsonProperty("tempat_lahir")
    private String tempatLahir;

    @JsonProperty("tgl_lahir")
    private LocalDate tglLahir;

    @JsonProperty("jns_kelamin")
    private Integer jnsKelamin;

    @JsonProperty("agama")
    private Integer agama;

    @JsonProperty("alamat_identitas")
    private String alamatIdentitas;

    @JsonProperty("provinsi_identitas")
    private String provinsiIdentitas;

    @JsonProperty("kota_identitas")
    private String kotaIdentitas;

    @JsonProperty("kecamatan_identitas")
    private String kecamatanIdentitas;

    @JsonProperty("desa_identitas")
    private String desaIdentitas;

    @JsonProperty("alamat_domisili")
    private String alamatDomisili;

    @JsonProperty("provinsi_domisili")
    private String provinsiDomisili;

    @JsonProperty("kota_domisili")
    private String kotaDomisili;

    @JsonProperty("kecamatan_domisili")
    private String kecamatanDomisili;

    @JsonProperty("desa_domisili")
    private String desaDomisili;

    @JsonProperty("telp")
    private String telp;

    @JsonProperty("pendidikan_terakhir")
    private Integer pendidikanTerakhir;

    @JsonProperty("status_kawin")
    private Character statusKawin;

    @JsonProperty("id_session")
    private String idSession;

    @JsonProperty("ip_login")
    private String ipLogin;

    @JsonProperty("flg_status")
    private Character flgStatus;

    @JsonProperty("tgl_status")
    private LocalDateTime tglStatus;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private String token;

    // Family (Kontak Kerabat) data
    private List<PesertaKontakRequest> kontak;

    // Education (Riwayat Pendidikan) data
    private List<PesertaPendidikanRequest> pesertaPendidikan;

    // Organisasi (Riwayat Organisasi) data
    private List<PesertaOrganisasiRequest> pesertaOrganisasi;

    // Pengalaman Kerja (Riwayat Kerja) data
    private List<PesertaPengalamanRequest> pesertaPengalaman;

}

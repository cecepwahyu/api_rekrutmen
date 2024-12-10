package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_peserta")
@Getter
@Setter
public class Peserta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peserta", nullable = false)
    private Integer idPeserta;

    @Column(name = "username", length = 50, nullable = false)
    @Size(max = 50, message = "Username cannot exceed 50 characters")
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    @Size(max = 100, message = "Password cannot exceed 50 characters")
    private String password;

    @Column(name = "nama", length = 50)
    @Size(max = 50, message = "Nama cannot exceed 50 characters")
    private String nama;

    @Column(name = "no_identitas", length = 16)
    private String noIdentitas;

    @Column(name = "tempat_lahir", length = 50)
    @Size(max = 50, message = "Tempat Lahir cannot exceed 50 characters")
    private String tempatLahir;

    @Column(name = "tgl_lahir")
    private LocalDate tglLahir;

    @Column(name = "jns_kelamin")
    private Integer jnsKelamin;

    @Column(name = "agama")
    private Integer agama;

    @Column(name = "alamat_identitas", length = 50)
    @Size(max = 50, message = "Alamat Identitas cannot exceed 50 characters")
    private String alamatIdentitas;

    @Column(name = "provinsi_identitas", length = 50)
    @Size(max = 50, message = "Provinsi cannot exceed 50 characters")
    private String provinsiIdentitas;

    @Column(name = "kota_identitas", length = 50)
    @Size(max = 50, message = "Kota cannot exceed 50 characters")
    private String kotaIdentitas;

    @Column(name = "kecamatan_identitas", length = 50)
    private String kecamatanIdentitas;

    @Column(name = "desa_identitas", length = 50)
    private String desaIdentitas;

    @Column(name = "alamat_domisili", length = 50)
    private String alamatDomisili;

    @Column(name = "provinsi_domisili", length = 50)
    private String provinsiDomisili;

    @Column(name = "kota_domisili", length = 50)
    private String kotaDomisili;

    @Column(name = "kecamatan_domisili", length = 50)
    private String kecamatanDomisili;

    @Column(name = "desa_domisili", length = 50)
    private String desaDomisili;

    @Column(name = "telp", length = 15)
    private String telp;

    @Column(name = "email")
    private String email;

    @Column(name = "pendidikan_terakhir", length = 50)
    private String pendidikanTerakhir;

    @Column(name = "status_kawin", length = 1)
    private Character statusKawin;

    @Column(name = "id_session", length = 100)
    private String idSession;

    @Column(name = "ip_login", length = 20)
    private String ipLogin;

    @Column(name = "flg_status", length = 1)
    private Character flgStatus;

    @Column(name = "tgl_status")
    private LocalDateTime tglStatus;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "token", length = 250)
    @Size(max = 250, message = "Token cannot exceed 250 characters")
    private String token;

    @Column(name = "otp", length = 6)
    private String otp;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "token_updated_at")
    private LocalDateTime tokenUpdatedAt;

    @Column(name = "otp_updated_at")
    private LocalDateTime otpUpdatedAt;
}
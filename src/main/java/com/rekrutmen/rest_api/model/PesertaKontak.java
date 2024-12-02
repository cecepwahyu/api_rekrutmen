package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "peserta_kontak")
@Getter
@Setter
public class PesertaKontak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kontak_peserta", nullable = false)
    private Integer idKontakPeserta;

    @Column(name = "id_peserta", length = 50, nullable = false)
    private Integer idPeserta;

    @Column(name = "nama_kontak", length = 50, nullable = false)
    private String namaKontak;

    @Column(name = "hub_kontak", length = 50)
    private String hubKontak;

    @Column(name = "telp_kontak", length = 50)
    private String telpKontak;

    @Column(name = "email_kontak", length = 50)
    private String emailKontak;

    @Column(name = "alamat_kontak")
    private String alamatKontak;
}

package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "peserta_pendidikan")
@Getter
@Setter
public class PesertaPendidikan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pendidikan", nullable = false)
    private Integer idPendidikan;

    @Column(name = "id_peserta", length = 50, nullable = false)
    private Integer idPeserta;

    @Column(name = "id_jenjang", length = 2, nullable = false)
    private String idJenjang;

    @Column(name = "nama_institusi", length = 50, nullable = false)
    private String namaInstitusi;

    @Column(name = "jurusan", length = 50)
    private String jurusan;

    @Column(name = "thn_masuk", length = 4)
    private String thnMasuk;

    @Column(name = "thn_lulus", length = 4)
    private String thnLulus;

    @Column(name = "nilai", length = 10)
    private String nilai;

    @Column(name = "gelar", length = 50)
    private String gelar;

    @Column(name = "achievements")
    private String achievements;
}

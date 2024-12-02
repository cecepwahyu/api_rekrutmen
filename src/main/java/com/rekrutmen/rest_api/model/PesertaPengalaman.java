package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "peserta_pengalaman")
@Getter
@Setter
public class PesertaPengalaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_data_kerja", nullable = false)
    private Integer idDataKerja;

    @Column(name = "id_peserta", length = 50, nullable = false)
    private Integer idPeserta;

    @Column(name = "nama_instansi", length = 50)
    private String namaInstansi;

    @Column(name = "posisi_kerja", length = 50)
    private String posisiKerja;

    @Column(name = "periode_kerja", length = 50)
    private String periodeKerja;

    @Column(name = "deskripsi_kerja")
    private String deskripsiKerja;
}

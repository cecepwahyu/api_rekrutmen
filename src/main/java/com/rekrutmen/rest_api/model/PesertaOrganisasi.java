package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "peserta_organisasi")
@Getter
@Setter
public class PesertaOrganisasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_org_peserta", nullable = false)
    private Integer idOrgPeserta;

    @Column(name = "id_peserta", length = 50, nullable = false)
    private Integer idPeserta;

    @Column(name = "nama_organisasi", length = 100, nullable = false)
    private String namaOrganisasi;

    @Column(name = "posisi_organisasi", length = 50)
    private String posisiOrganisasi;

    @Column(name = "periode", length = 50)
    private String periode;

    @Column(name = "deskripsi_kerja")
    private String deskripsiKerja;

    @Column(name = "sertifikat")
    private String sertifikat;
}

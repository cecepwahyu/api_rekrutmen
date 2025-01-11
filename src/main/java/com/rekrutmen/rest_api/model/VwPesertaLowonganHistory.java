package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "vw_peserta_lowongan_history") // Matches the view name
public class VwPesertaLowonganHistory {

    @Id
    @Column(name = "id_aplikasi")
    private String idAplikasi;

    @Column(name = "id_peserta", nullable = false)
    private Long idPeserta;

    @Column(name = "nama_peserta", nullable = false, length = 100)
    private String namaPeserta;

    @Column(name = "email_peserta", nullable = false, length = 100)
    private String emailPeserta;

    @Column(name = "no_identitas_peserta", nullable = false, length = 50)
    private String noIdentitasPeserta;

    @Column(name = "id_lowongan", nullable = false)
    private Long idLowongan;

    @Column(name = "judul_lowongan", nullable = false, length = 200)
    private String judulLowongan;

    @Column(name = "posisi_lowongan", nullable = false, length = 100)
    private String posisiLowongan;

    @Column(name = "lowongan_periode_awal", nullable = false)
    private LocalDate lowonganPeriodeAwal;

    @Column(name = "lowongan_periode_akhir", nullable = false)
    private LocalDate lowonganPeriodeAkhir;

    @Column(name = "tanggal_aplikasi", nullable = false)
    private LocalDate tanggalAplikasi;

    @Column(name = "status_aplikasi", nullable = false, length = 50)
    private String statusAplikasi;

    @Column(name = "last_status_update", nullable = false)
    private LocalDateTime lastStatusUpdate;

    @Column(name = "tahun_aplikasi", nullable = false)
    private Integer tahunAplikasi;

    @Column(name = "slug", nullable = false, length = 255)
    private String slug;

    @Column(name = "status")
    private Character status;

    @Column(name = "is_rekrutmen")
    private Boolean isRekrutmen;
}

package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "lowongan_peserta_documents")
public class LowonganPesertaDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_peserta_lowongan", nullable = false, length = 20)
    private String idPesertaLowongan;

    @Column(name = "id_user_document", nullable = false)
    private Integer idUserDocument;

    @Column(name = "sudah_diverifikasi", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean sudahDiverifikasi = false;

    @Column(name = "tanggal_verifikasi")
    private LocalDateTime tanggalVerifikasi;

    @Column(name = "diverifikasi_oleh")
    private Integer diverifikasiOleh;

    @Column(name = "status_verifikasi", nullable = false, length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'Pending'")
    private String statusVerifikasi = "Pending";

    @Column(name = "catatan_verifikasi", columnDefinition = "TEXT")
    private String catatanVerifikasi;
}

package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "progres_tahapan")
@Getter
@Setter
public class ProgresTahapan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "id_peserta_lowongan", length = 20, nullable = false)
    private String idPesertaLowongan;

    @Column(name = "id_tahapan", nullable = false)
    private Integer idTahapan;

    @Column(name = "status_tahapan", length = 50, nullable = false, columnDefinition = "varchar(50) default 'Pending'")
    private String statusTahapan = "Pending";

    @Column(name = "waktu_diperbarui", columnDefinition = "timestamp default now()")
    private LocalDateTime waktuDiperbarui = LocalDateTime.now();

    @Column(name = "id_lowongan")
    private Integer idLowongan;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "flg_tahapan")
    private Boolean flgTahapan;

    @Column(name = "prev_tahapan")
    private Integer prevTahapan;

    @Column(name = "next_tahapan")
    private Integer nextTahapan;

    @Column(name = "prev_sort_order")
    private Integer prevSortOrder;

    @Column(name = "next_sort_order")
    private Integer nextSortOrder;

    @Column(name = "lolos_next_stage")
    private Boolean lolosNextStage;

    @Column(name = "current_sort_order")
    private Integer currentSortOrder;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tahapan", referencedColumnName = "id_tahapan", insertable = false, updatable = false)
    private TahapanSeleksi tahapanSeleksi;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lowongan", referencedColumnName = "id_lowongan", insertable = false, updatable = false)
    private Lowongan lowongan;


}

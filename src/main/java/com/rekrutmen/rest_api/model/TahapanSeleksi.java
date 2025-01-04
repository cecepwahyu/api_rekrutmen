package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_tahapan_seleksi")
@Getter
@Setter
public class TahapanSeleksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tahapan", nullable = false)
    private Integer idTahapan;

    @Column(name = "nama_tahapan", nullable = false, length = 100)
    private String namaTahapan;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}

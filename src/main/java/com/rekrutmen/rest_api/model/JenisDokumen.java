package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_jenis_dokumen")
@Getter
@Setter
public class JenisDokumen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dokumen", nullable = false)
    private Integer idDokumen;

    @Column(name = "nama_dokumen", nullable = false, length = 100)
    private String namaDokumen;

    @Column(name = "deskripsi")
    private String deskripsi;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "max_size_mb")
    private Integer maxSizeMb;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "tipe_file", length = 2)
    private String tipeFile;
}

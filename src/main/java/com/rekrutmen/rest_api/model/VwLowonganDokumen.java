package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vw_lowongan_dokumen")
public class VwLowonganDokumen {

    @Id
    @Column(name = "lowongan_id", nullable = false)
    private Integer lowonganId;

    @Column(name = "dokumen_id", nullable = false)
    private Integer dokumenId;

    @Column(name = "is_required", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isRequired;

    @Column(name = "nama_dokumen", nullable = false)
    private String namaDokumen;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "max_size_mb", nullable = false)
    private Integer maxSizeMb;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isActive;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "slug", nullable = false)
    private String slug;
}

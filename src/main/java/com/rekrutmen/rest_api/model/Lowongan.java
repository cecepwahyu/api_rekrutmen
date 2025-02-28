package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        name = "tbl_lowongan",
        uniqueConstraints = @UniqueConstraint(name = "unique_slug", columnNames = {"slug", "status"})
)
public class Lowongan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lowongan", nullable = false)
    private Integer idLowongan;

    @Column(name = "judul_lowongan", nullable = false, length = 100)
    private String judulLowongan;

    @Column(name = "slug", nullable = false, length = 255)
    private String slug;

    @Column(name = "posisi", nullable = false, length = 100)
    private String posisi;

    @Column(name = "tentang_pekerjaan", nullable = false, columnDefinition = "TEXT")
    private String tentangPekerjaan;

    @Column(name = "persyaratan", nullable = false, columnDefinition = "TEXT")
    private String persyaratan;

    @Column(name = "periode_awal", nullable = false)
    private LocalDate periodeAwal;

    @Column(name = "periode_akhir", nullable = false)
    private LocalDate periodeAkhir;

    @Column(name = "status", nullable = false, length = 1, columnDefinition = "CHAR DEFAULT '0'")
    private String status;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "updated_by", nullable = false)
    private Integer updatedBy;

    @Column(name = "kode_lowongan")
    private String kodeLowongan;

    @Column(name = "flg_approve")
    private Boolean flgApprove;

    @Column(name = "id_auth")
    private Integer idAuth;

    @Column(name = "min_height")
    private Integer minHeight;

    @Column(name = "is_height_mandatory", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isHeightMandatory;

    @Column(name = "max_age")
    private String maxAge;
}

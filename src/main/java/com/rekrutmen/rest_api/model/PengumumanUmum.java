package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tbl_pengumuman_umum")
public class PengumumanUmum {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String judul;

    @Column(nullable = false, length = 255, unique = true)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String isi;

    @Column(nullable = false)
    private boolean status = true;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", nullable = false)
    private Integer updatedBy;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;
}

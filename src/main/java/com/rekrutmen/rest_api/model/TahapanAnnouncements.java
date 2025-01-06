package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tahapan_announcements")
@Getter
@Setter
public class TahapanAnnouncements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_announcements", nullable = false)
    private Integer idAnnouncements;

    @Column(name = "id_tahapan", nullable = false)
    private Integer idTahapan;

    @Column(name = "id_lowongan", nullable = false)
    private Integer idLowongan;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "status")
    private Character status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;
}

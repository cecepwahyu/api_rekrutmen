package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_riwayat_pendidikan")
@Getter
@Setter
public class RiwayatPendidikan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pendidikanId;

    @Column(length = 50)
    private Integer profileId;

    @Column(length = 100)
    private String institutionName;

    @Column(length = 50)
    private String degree;

    @Column(length = 100)
    private String fieldOfStudy;

    @Column(length = 100)
    private LocalDate startDate;

    private LocalDate endDate;

    private String achievements;
}

package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_pengalaman_kerja")
@Getter
@Setter
public class PengalamanKerja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pengalamanId;

    @Column(length = 50)
    private Integer profileId;

    @Column(length = 100)
    private String companyName;

    @Column(length = 50)
    private String position;

    private LocalDate startDate;

    private LocalDate endDate;

    private String responsibilities;
}

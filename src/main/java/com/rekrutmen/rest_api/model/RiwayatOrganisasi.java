package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_riwayat_organisasi")
@Getter
@Setter
public class RiwayatOrganisasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer organisasiId;

    @Column(length = 50)
    private Integer profileId;

    @Column(length = 100)
    private String organizationName;

    @Column(length = 50)
    private String role;

    private LocalDate startDate;

    private LocalDate endDate;

    private String responsibilities;
}

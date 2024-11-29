package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_referensi")
@Getter
@Setter
public class Referensi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer refIdx;

    @Column(length = 50)
    private String refGroup1;

    @Column(length = 50)
    private String refGroup2;

    @Column(length = 50)
    private String refCode;

    @Column(length = 50)
    private String refDesc;

    @Column(length = 50)
    private String refCode2;

    @Column(length = 50)
    private String refDesc2;
}

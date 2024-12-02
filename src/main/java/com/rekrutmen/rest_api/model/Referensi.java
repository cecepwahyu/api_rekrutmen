package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_referensi")
@Getter
@Setter
public class Referensi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref_idx", nullable = false)
    private Integer refIdx;

    @Column(name = "ref_group1", length = 20)
    private String refGroup1;

    @Column(name = "ref_group2", length = 20)
    private String refGroup2;

    @Column(name = "ref_code", length = 50)
    private String refCode;

    @Column(name = "ref_desc", length = 50)
    private String refDesc;

    @Column(name = "ref_code2", length = 50)
    private String refCode2;

    @Column(name = "ref_desc2", length = 50)
    private String refDesc2;
}

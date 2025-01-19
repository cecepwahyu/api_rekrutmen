package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tbl_kerabat")
@Getter
@Setter
public class Kerabat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kerabatId;

    @Column(length = 50)
    private Integer profileId;

    @Column(length = 16)
    private String name;

    @Column(length = 50)
    private String relationship;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    private String address;
}

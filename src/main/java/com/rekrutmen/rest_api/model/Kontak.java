package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "peserta_kontak")
@Getter
@Setter
public class Kontak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idKontakPeserta;

    @Column(length = 50)
    private Integer idPeserta;

    @Column(length = 50)
    private String namaKontak;

    @Column(length = 50)
    private String hubKontak;

    @Column(length = 50)
    private String telpKontak;

    @Column(length = 50)
    private String emailKontak;

    private String alamatKontak;
}

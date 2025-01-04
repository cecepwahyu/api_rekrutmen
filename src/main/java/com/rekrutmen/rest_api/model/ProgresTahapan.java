package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "progres_tahapan")
@Getter
@Setter
public class ProgresTahapan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "id_tahapan", length = 50, nullable = false)
    private Integer idTahapan;

    @Column(name = "status_tahapan", length = 50)
    private String statusTahapan;

    @Column(name = "waktu_diperbarui", length = 50)
    private LocalDateTime waktuDiperbarui;

}

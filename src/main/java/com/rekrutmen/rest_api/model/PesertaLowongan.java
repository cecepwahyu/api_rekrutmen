package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "peserta_lowongan")
@Getter
@Setter
public class PesertaLowongan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

//    @Column(name = "id_lowongan", nullable = false)
    @Column(name = "id_lowongan")
    private Integer idLowongan;

    //@Column(name = "id_peserta", length = 50, nullable = false)
    @Column(name = "id_peserta", length = 50)
    private Integer idPeserta;

    @Column(name = "tanggal_aplikasi")
    private LocalDateTime tanggalAplikasi;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "last_status_update", length = 50)
    private LocalDateTime lastStatusUpdate;

    @Column(name = "is_rekrutmen")
    private Boolean isRekrutmen;

}

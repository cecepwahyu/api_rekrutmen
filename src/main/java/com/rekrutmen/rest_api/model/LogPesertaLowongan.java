package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_peserta_lowongan")
@Getter
@Setter
public class LogPesertaLowongan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id;

    @Column(name = "id_peserta", nullable = false)
    private Integer idPeserta;

    @Column(name = "id_lowongan", nullable = false)
    private Integer idLowongan;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

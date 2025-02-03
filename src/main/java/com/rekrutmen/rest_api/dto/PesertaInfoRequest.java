package com.rekrutmen.rest_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PesertaInfoRequest {

    private String nama;
    private String username;
    private String noIdentitas;
    private String kodeLowongan;
    private String judulLowongan;
    private String profilePicture;
    private String idLowongan;
    private LocalDateTime tanggalAplikasi;

    // Constructor matching the query
    public PesertaInfoRequest(String nama, String username, String noIdentitas, String kodeLowongan, String judulLowongan, String profilePicture, String idLowongan, LocalDateTime tanggalAplikasi) {
        this.nama = nama;
        this.username = username;
        this.noIdentitas = noIdentitas;
        this.kodeLowongan = kodeLowongan;
        this.judulLowongan = judulLowongan;
        this.profilePicture = profilePicture;
        this.idLowongan = idLowongan;
        this.tanggalAplikasi = tanggalAplikasi;
    }
}

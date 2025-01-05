package com.rekrutmen.rest_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesertaInfoRequest {

    private String nama;
    private String username;
    private String kodeLowongan;
    private String judulLowongan;
    private String profilePicture;
    private String idLowongan;

    // Constructor matching the query
    public PesertaInfoRequest(String nama, String username, String kodeLowongan, String judulLowongan, String profilePicture, String idLowongan) {
        this.nama = nama;
        this.username = username;
        this.kodeLowongan = kodeLowongan;
        this.judulLowongan = judulLowongan;
        this.profilePicture = profilePicture;
        this.idLowongan = idLowongan;
    }
}

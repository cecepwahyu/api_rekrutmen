package com.rekrutmen.rest_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesertaInfoRequest {

    private String nama;
    private String username;
    private String lowonganId;
    private String judulLowongan;
    private String profilePicture;

    // Constructor matching the query
    public PesertaInfoRequest(String nama, String username, String lowonganId, String judulLowongan, String profilePicture) {
        this.nama = nama;
        this.username = username;
        this.lowonganId = lowonganId;
        this.judulLowongan = judulLowongan;
        this.profilePicture = profilePicture;
    }
}

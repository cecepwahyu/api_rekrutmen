package com.rekrutmen.rest_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesertaDataRequest {

    private String nama;
    private String email;
    private String profilePicture;

    // Constructor matching the query
    public PesertaDataRequest(String nama, String email, String profilePicture) {
        this.nama = nama;
        this.email = email;
        this.profilePicture = profilePicture;
    }
}

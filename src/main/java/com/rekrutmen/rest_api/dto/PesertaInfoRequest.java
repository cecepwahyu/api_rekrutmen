package com.rekrutmen.rest_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesertaInfoRequest {

    private String nama;
    private String username;
    private String noPeserta;

    // Constructor matching the query
    public PesertaInfoRequest(String nama, String username, String noPeserta) {
        this.nama = nama;
        this.username = username;
        this.noPeserta = noPeserta;
    }
}

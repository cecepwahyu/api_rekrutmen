package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesertaKontakRequest {

    @JsonProperty("nama_kontak")
    private String namaKontak;

    @JsonProperty("hub_kontak")
    private String hubKontak;

    @JsonProperty("telp_kontak")
    private String telpKontak;

    @JsonProperty("email_kontak")
    private String emailKontak;

    @JsonProperty("alamat_kontak")
    private String alamatKontak;
}

package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PesertaKontakRequest {

    @JsonProperty("id_kontak_peserta")
    private Integer idKontakPeserta;

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

    @JsonProperty("is_bpddiy_related")
    private Boolean isBpddiyRelated;
}

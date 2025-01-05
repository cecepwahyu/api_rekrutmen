package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesertaPendidikanRequest {

    @JsonProperty("id_jenjang")
    @NotNull
    private String idJenjang;

    @JsonProperty("nama_institusi")
    @NotNull
    private String namaInstitusi;

    @JsonProperty("jurusan")
    private String jurusan;

    @JsonProperty("thn_lulus")
    private String thnLulus;

    @JsonProperty("nilai")
    private String nilai;

    @JsonProperty("gelar")
    private String gelar;

    @JsonProperty("thn_masuk")
    private String thnMasuk;

    @JsonProperty("achievements")
    private String achievements;
}

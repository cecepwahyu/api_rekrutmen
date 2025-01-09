package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesertaOrganisasiRequest {

    @JsonProperty("nama_organisasi")
    private String namaOrganisasi;

    @JsonProperty("posisi_organisasi")
    private String posisiOrganisasi;

    @JsonProperty("periode")
    private String periode;

    @JsonProperty("deskripsi_kerja")
    private String deskripsiKerja;

    @JsonProperty("sertifikat")
    private String sertifikat;
}

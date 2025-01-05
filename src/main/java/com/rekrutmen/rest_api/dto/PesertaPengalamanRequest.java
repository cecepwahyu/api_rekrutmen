package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesertaPengalamanRequest {

    @JsonProperty("nama_instansi")
    private String namaInstansi;

    @JsonProperty("posisi_kerja")
    private String posisiKerja;

    @JsonProperty("periode_kerja")
    private String periodeKerja;

    @JsonProperty("deskripsi_kerja")
    private String deskripsiKerja;

    @JsonProperty("surat_pengalaman_kerja")
    private String suratPengalamanKerja;
}

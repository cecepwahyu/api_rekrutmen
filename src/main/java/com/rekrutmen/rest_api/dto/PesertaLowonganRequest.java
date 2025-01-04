package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PesertaLowonganRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("id_lowongan")
    private Integer idLowongan;

    @JsonProperty("id_peserta")
    private Integer idPeserta;

    @JsonProperty("tanggal_aplikasi")
    private LocalDateTime tanggalAplikasi;

    @JsonProperty("status")
    private String status;

    @JsonProperty("last_status_update")
    private LocalDateTime lastStatusUpdate;

    @JsonProperty("tahun_aplikasi")
    private Integer tahunAplikasi;

    @JsonProperty("id_user_documents")
    private List<Integer> idUserDocuments; // Added this field to handle associated user documents
}

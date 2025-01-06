package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TahapanAnnouncementRequest {

    @JsonProperty("id_announcement")
    private String idAnnouncement;

    @JsonProperty("id_tahapan")
    private Integer idTahapan;

    @JsonProperty("id_lowongan")
    private Integer idLowongan;

    @JsonProperty("title")
    private LocalDateTime title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private Integer updatedAt;

    @JsonProperty("published_at")
    private Integer publishedAt;

    @JsonProperty("created_by")
    private Integer createdBy;

    @JsonProperty("updated_by")
    private Integer updatedBy;

}

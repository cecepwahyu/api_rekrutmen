package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitDocumentRequest {

    @NotBlank(message = "Document data cannot be empty")
    @JsonProperty("document_data")
    private String documentData;

    @NotBlank(message = "File name cannot be empty")
    @JsonProperty("file_name")
    private String fileName;

    @NotBlank(message = "File type cannot be empty")
    @JsonProperty("file_type")
    private String fileType;
}

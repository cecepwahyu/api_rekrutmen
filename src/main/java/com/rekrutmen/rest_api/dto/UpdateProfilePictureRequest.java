package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfilePictureRequest {

    @NotBlank(message = "Base64 image cannot be empty")
    @JsonProperty("base64_image")
    private String base64Image;
}
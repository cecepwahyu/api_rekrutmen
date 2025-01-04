package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendOtpRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("no_identitas")
    private String noIdentitas;

}

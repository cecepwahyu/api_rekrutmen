package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    @Email
    @Pattern(regexp = "^[^\\s]+$")
    @JsonProperty("email")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[^\\s]+$")
    @JsonProperty("password")
    private String password;
}

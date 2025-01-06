package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {

    @NotBlank(message = "Email cannot be blank")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "New password cannot be blank")
    @JsonProperty("new_password")
    private String newPassword;

    @NotBlank(message = "Confirm password cannot be blank")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
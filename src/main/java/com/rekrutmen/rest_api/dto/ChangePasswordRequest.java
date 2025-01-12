package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank(message = "Email cannot be blank")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Current password cannot be blank")
    @JsonProperty("currentPassword")
    private String currentPassword;

    @NotBlank(message = "New password cannot be blank")
    @JsonProperty("newPassword")
    private String newPassword;

    @NotBlank(message = "Confirm password cannot be blank")
    @JsonProperty("confirmPassword")
    private String confirmPassword;
}

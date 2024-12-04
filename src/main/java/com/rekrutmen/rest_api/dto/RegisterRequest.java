package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    @Pattern(regexp = "^[^\\s]+$",
            message = "Invalid payload request"
    )
    @JsonProperty("username")
    private String username;

    @NotBlank
    @Pattern(
            regexp = "^[^\\s]+$",
            message = "Invalid payload request"
    )
    @JsonProperty("password")
    private String password;

    @NotBlank
    @Email
    @Pattern(regexp = "^[^\\s]+$",
            message = "Invalid payload request"
    )
    @JsonProperty("email")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[0-9]{16}$",
            message = "Invalid payload request"
    )
    @JsonProperty("no_identitas")
    private String noIdentitas;
}

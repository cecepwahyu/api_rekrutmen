package com.rekrutmen.rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
public class AccountVerificationRequest {

    @NotBlank
    @NumberFormat
    @Pattern(
            regexp = "^\\d{6}$",
            message = "Invalid payload request"
    )

    @JsonProperty("otp")
    private String otp;

}

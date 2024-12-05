package com.rekrutmen.rest_api.util;

import com.rekrutmen.rest_api.dto.ResetPasswordRequest;
import com.rekrutmen.rest_api.dto.OtpVerificationRequest;
import com.rekrutmen.rest_api.exception.InvalidRequestExceptionHandler;
import org.springframework.util.StringUtils;

public class RequestValidatorUtil {

    private static final ResponseCodeUtil responseCodeUtil = new ResponseCodeUtil();

    public static void validateResetPasswordRequest(ResetPasswordRequest request) {
        if (!StringUtils.hasText(request.getEmail())) {
            throw new InvalidRequestExceptionHandler("270", responseCodeUtil.getMessage("270") + " ('email' is required).");
        }
        if (!StringUtils.hasText(request.getNoIdentitas())) {
            throw new InvalidRequestExceptionHandler("270", responseCodeUtil.getMessage("270") + " ('no_identitas' is required).");
        }
    }

    public static void validateOtpVerificationRequest(OtpVerificationRequest request) {
        if (!StringUtils.hasText(request.getOtp())) {
            throw new InvalidRequestExceptionHandler("270", responseCodeUtil.getMessage("270") + " ('otp' is required).");
        }
    }
}

package com.rekrutmen.rest_api.exception;

public class InvalidRequestExceptionHandler extends RuntimeException {
    private final String responseCode;

    public InvalidRequestExceptionHandler(String responseCode, String responseMessage) {
        super(responseMessage);
        this.responseCode = responseCode;
    }

    public String getCode() {
        return responseCode;
    }
}

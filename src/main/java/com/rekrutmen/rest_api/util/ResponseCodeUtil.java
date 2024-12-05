package com.rekrutmen.rest_api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseCodeUtil {
    private Map<String, String> responseCode;
    private Map<String, String> responseMessages;

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load JSON from resources
            responseCode = objectMapper.readValue(
                    getClass().getClassLoader().getResourceAsStream("response_codes.json"),
                    Map.class
            );
            responseMessages = objectMapper.readValue(
                    getClass().getClassLoader().getResourceAsStream("response_codes.json"),
                    Map.class
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load response_codes.json", e);
        }
    }

    public String getCode(String code) {
        return responseCode.containsKey(code) ? code : "Unknown Error Code";
    }

    public String getMessage(String code) {
        return responseMessages.getOrDefault(code, "Unknown Error Code");
    }
}

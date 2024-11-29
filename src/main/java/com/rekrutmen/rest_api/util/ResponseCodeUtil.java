package com.rekrutmen.rest_api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class ResponseCodeUtil {
    private Map<String, String> responseMessages;

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load JSON from resources
            responseMessages = objectMapper.readValue(
                    getClass().getClassLoader().getResourceAsStream("response_codes.json"),
                    Map.class
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load response_codes.json", e);
        }
    }

    public String getMessage(String code) {
        return responseMessages.getOrDefault(code, "Unknown Error Code");
    }
}

package com.rekrutmen.rest_api.helper;

import com.rekrutmen.rest_api.model.Peserta;

import java.util.regex.Pattern;

public class ValidationHelper {

    // Regular expression for email validation
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    /**
     * Validates the user input for login.
     * @param peserta The User object containing email and password.
     * @return A validation error message if invalid, or null if valid.
     */
    public static String validateLoginRequest(Peserta peserta) {
        if (peserta == null) {
            return "Request body cannot be null.";
        }

        if (peserta.getEmail() == null || peserta.getEmail().trim().isEmpty()) {
            return "Email is required.";
        }

        if (peserta.getPassword() == null || peserta.getPassword().trim().isEmpty()) {
            return "Password is required.";
        }

        if (!Pattern.matches(EMAIL_REGEX, peserta.getEmail())) {
            return "Invalid email format.";
        }

        if (peserta.getPassword().length() < 5) {
            return "Password must be at least 5 characters long.";
        }

        return null;
    }
}
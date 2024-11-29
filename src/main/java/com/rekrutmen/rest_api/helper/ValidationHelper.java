package com.rekrutmen.rest_api.helper;

import com.rekrutmen.rest_api.model.User;

import java.util.regex.Pattern;

public class ValidationHelper {

    // Regular expression for email validation
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    /**
     * Validates the user input for login.
     * @param user The User object containing email and password.
     * @return A validation error message if invalid, or null if valid.
     */
    public static String validateLoginRequest(User user) {
        if (user == null) {
            return "Request body cannot be null.";
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "Email is required.";
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "Password is required.";
        }

        if (!Pattern.matches(EMAIL_REGEX, user.getEmail())) {
            return "Invalid email format.";
        }

        if (user.getPassword().length() < 5) {
            return "Password must be at least 5 characters long.";
        }

        return null;
    }
}
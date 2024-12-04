package com.rekrutmen.rest_api.util;

public class MaskingUtil {

    private MaskingUtil() {

    }

    /**
     * Masks a given password for logging purposes.
     *
     * @param password the original password
     * @return the masked password
     */
    public static String maskPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "****";
        }

        int length = password.length();
        if (length <= 2) {
            return "*".repeat(length);
        }
        return password.charAt(0) + "*".repeat(length - 2) + password.charAt(length - 1);
    }
}

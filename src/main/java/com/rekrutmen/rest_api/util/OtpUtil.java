package com.rekrutmen.rest_api.util;

import java.util.Random;

public class OtpUtil {

    private OtpUtil() {
    }

    public static String generateOtp() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }
}
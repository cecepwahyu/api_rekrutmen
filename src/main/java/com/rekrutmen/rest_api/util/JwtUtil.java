package com.rekrutmen.rest_api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    /**
     * Generates a JWT token with custom claims.
     *
     * @param claims A map containing the custom claims to include in the token.
     * @return A signed JWT token.
     */
    public static String generateTokenWithClaims(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Parses a JWT token and retrieves the claims.
     *
     * @param token The JWT token to parse.
     * @return Claims contained in the token.
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new RuntimeException("Invalid JWT signature: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JWT token: " + e.getMessage(), e);
        }
    }


    /**
     * Validates a JWT token and checks if it is not expired.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid and not expired, false otherwise.
     */
    public static boolean isTokenValid(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static Map<String, String> decodeToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token format.");
            }

            // Decode the header
            String header = new String(Base64.getUrlDecoder().decode(parts[0]));

            // Decode the payload
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

            // Signature (not decoded, as it is just a hash)
            String signature = parts[2];

            // Return the decoded parts
            return Map.of(
                    "header", header,
                    "payload", payload,
                    "signature", signature
            );
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Error decoding JWT token: " + e.getMessage(), e);
        }
    }
}

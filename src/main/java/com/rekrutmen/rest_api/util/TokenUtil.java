package com.rekrutmen.rest_api.util;

import com.rekrutmen.rest_api.model.Peserta;
import com.rekrutmen.rest_api.repository.PesertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class TokenUtil {

    @Autowired
    private PesertaRepository pesertaRepository;

    /**
     * Validates whether the provided token exists in the database and is not expired.
     *
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean isValidToken(String token) {
        if (token == null || !token.startsWith("Bearer ") || token.length() <= 7) {
            return false;
        }

        String actualToken = token.replace("Bearer ", "").trim();

        // Query the database for the token
        Optional<Peserta> peserta = pesertaRepository.findByToken(actualToken);

        if (peserta.isEmpty()) {
            return false;
        }

        // Check if the token is expired
        Peserta foundPeserta = peserta.get();
        return foundPeserta.getUpdatedAt() != null && !isTokenExpired(foundPeserta.getUpdatedAt());
    }

    /**
     * Checks if the token is expired based on the last updated time.
     *
     * @param updatedAt the timestamp of the token's last update
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(LocalDateTime updatedAt) {
        // Assume token expiration is 1 hour after the updated_at timestamp
        LocalDateTime expirationTime = updatedAt.plusHours(24);
        return LocalDateTime.now().isAfter(expirationTime);
    }
}

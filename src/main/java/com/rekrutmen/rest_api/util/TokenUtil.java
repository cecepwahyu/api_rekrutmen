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
     * Validates whether the provided token exists in the database.
     *
     * @param token the token to validate
     * @return true if the token exists and is valid, false otherwise
     */
    public boolean isValidToken(String token) {
        if (token == null || !token.startsWith("Bearer ") || token.length() <= 7) {
            return false;
        }

        String actualToken = token.replace("Bearer ", "").trim();
        return pesertaRepository.findByToken(actualToken).isPresent();
    }

    /**
     * Checks if the token is expired based on the token's tokenUpdatedAt timestamp.
     *
     * @param token the token to validate
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        if (token == null || !token.startsWith("Bearer ") || token.length() <= 7) {
            return true; // Invalid tokens are treated as expired
        }

        String actualToken = token.replace("Bearer ", "").trim();

        // Query the database for the token's updated_at field
        Optional<Peserta> peserta = pesertaRepository.findByToken(actualToken);

        // Directly validate the `updatedAt` field
        if (peserta.isPresent()) {
            LocalDateTime tokenUpdatedAt = peserta.get().getTokenUpdatedAt();
            return tokenUpdatedAt == null || isTokenExpired(tokenUpdatedAt);
        }

        // Return true if no `updatedAt` is found
        return true;
    }

    /**
     * Extracts Peserta ID from the token stored in the database.
     *
     * @param token the authorization token
     * @return the pesertaId if found, otherwise null
     */
    public Integer extractPesertaId(String token) {
        if (token == null || !token.startsWith("Bearer ") || token.length() <= 7) {
            System.out.println("Invalid or empty token received.");
            return null;
        }

        String actualToken = token.replace("Bearer ", "").trim();
        Optional<Peserta> peserta = pesertaRepository.findByToken(actualToken);

        if (peserta.isPresent()) {
            Peserta foundPeserta = peserta.get();
            return foundPeserta.getIdPeserta();
        } else {
            System.out.println("No Peserta found for token: " + actualToken);
        }

        return null;
    }


    /**
     * Checks if the token is expired based on the last updated time.
     *
     * @param tokenUpdatedAt the timestamp of the token's last update
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(LocalDateTime tokenUpdatedAt) {
        LocalDateTime expirationTime = tokenUpdatedAt.plusHours(24);
        return LocalDateTime.now().isAfter(expirationTime);
    }
}

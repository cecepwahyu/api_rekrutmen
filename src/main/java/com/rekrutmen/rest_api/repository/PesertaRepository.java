package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.Peserta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PesertaRepository extends JpaRepository<Peserta, Long> {
    Optional<Peserta> findByEmail(String email);
    //boolean existsByNik(String noIdentitas);
    Optional<Peserta> findByIdPeserta(Integer idPeserta);
    Optional<Peserta> findByEmailAndNoIdentitas(String email, String noIdentitas);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByTelp(String telp);
    boolean existsByNoIdentitas(String noIdentitas);
    Optional<Peserta> findByToken(String token);
}


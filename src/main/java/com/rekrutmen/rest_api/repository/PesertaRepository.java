package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.dto.PesertaInfoRequest;
import com.rekrutmen.rest_api.model.Peserta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PesertaRepository extends JpaRepository<Peserta, Long> {
    Optional<Peserta> findByEmail(String email);
    Optional<Peserta> findByIdPeserta(Integer idPeserta);
    Optional<Peserta> findByEmailAndNoIdentitas(String email, String noIdentitas);
    Optional<Peserta> findByToken(String token);
    Optional<Peserta> findByOtp(String otp);
    Optional<Peserta> findByTelp(String telp);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByTelp(String telp);
    boolean existsByNoIdentitas(String noIdentitas);

    @Query("SELECT new com.rekrutmen.rest_api.dto.PesertaInfoRequest(p.nama, p.email, p.noPeserta) FROM Peserta p WHERE p.idPeserta = :idPeserta")
    Optional<PesertaInfoRequest> findPesertaInfoByIdPeserta(@Param("idPeserta") Integer idPeserta);
}


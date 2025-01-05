package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.dto.PesertaInfoRequest;
import com.rekrutmen.rest_api.model.Peserta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    //Get Peserta Info
    @Query("SELECT new com.rekrutmen.rest_api.dto.PesertaInfoRequest(p.nama, p.email, CAST(pl.id AS string), l.judulLowongan, p.profilePicture, CAST(l.idLowongan AS string)) " +
            "FROM Peserta p " +
            "JOIN PesertaLowongan pl ON p.idPeserta = pl.idPeserta " +
            "JOIN Lowongan l ON pl.idLowongan = l.idLowongan " +
            "WHERE p.idPeserta = :idPeserta")
    Optional<PesertaInfoRequest> findPesertaInfoByIdPeserta(@Param("idPeserta") Integer idPeserta);

    //Get Peserta Info
//    @Query("SELECT new com.rekrutmen.rest_api.dto.PesertaInfoRequest(p.nama, p.email, p.profilePicture) " +
//            "FROM Peserta p " +
//            "WHERE p.idPeserta = :idPeserta")
//    Optional<PesertaInfoRequest> findPesertaDataByIdPeserta(@Param("idPeserta") Integer idPeserta);

    @Query("SELECT p.nama, p.email, p.profilePicture " +
            "FROM Peserta p " +
            "WHERE p.idPeserta = :idPeserta")
    Optional<Object[]> findPesertaDataByIdPesertaRaw(@Param("idPeserta") Integer idPeserta);

    // Update profile picture
    @Modifying
    @Query("UPDATE Peserta p SET p.profilePicture = :profilePicture WHERE p.idPeserta = :idPeserta")
    void updateProfilePicture(@Param("idPeserta") Integer idPeserta, @Param("profilePicture") String profilePicture);

}


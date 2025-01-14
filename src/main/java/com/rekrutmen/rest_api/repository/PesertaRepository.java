package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.dto.PesertaInfoRequest;
import com.rekrutmen.rest_api.model.Peserta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PesertaRepository extends JpaRepository<Peserta, Long> {
    Optional<Peserta> findByEmail(String email);
    Optional<Peserta> findByIdPeserta(Integer idPeserta);
    Optional<Peserta> findByEmailAndNoIdentitas(String email, String noIdentitas);
    Optional<Peserta> findByToken(String token);
    Optional<Peserta> findByOtp(String otp);
    Optional<Peserta> findByTelp(String telp);
    //boolean existsByUsername(String username);
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

    @Query("SELECT p.nama, p.email, p.profilePicture " +
            "FROM Peserta p " +
            "WHERE p.idPeserta = :idPeserta")
    Optional<Object[]> findPesertaDataByIdPesertaRaw(@Param("idPeserta") Integer idPeserta);

    // Update profile picture
    @Modifying
    @Query("UPDATE Peserta p SET p.profilePicture = :profilePicture WHERE p.idPeserta = :idPeserta")
    void updateProfilePicture(@Param("idPeserta") Integer idPeserta, @Param("profilePicture") String profilePicture);

    @Modifying
    @Query("UPDATE Peserta p SET p.isFinal = true, p.updatedAt = :updatedAt WHERE p.idPeserta = :idPeserta")
    void setPesertaIsFinal(@Param("idPeserta") Integer idPeserta, @Param("updatedAt") LocalDateTime updatedAt);

    @Query(value = """
    SELECT 
        p.id_peserta AS peserta_id,
        p.profile_picture AS profile_picture,
        po.id_org_peserta AS organisasi_id, po.nama_organisasi, po.posisi_organisasi, 
        po.periode AS organisasi_periode, po.deskripsi_kerja AS organisasi_deskripsi,
        pp.id_pendidikan AS pendidikan_id, pp.id_jenjang AS pendidikan_jenjang, pp.nama_institusi,
        pp.jurusan, pp.thn_masuk, pp.thn_lulus, pp.nilai, pp.gelar, pp.achievements,
        pg.id_data_kerja AS pengalaman_id, pg.nama_instansi, pg.posisi_kerja,
        pg.periode_kerja, pg.deskripsi_kerja AS pengalaman_deskripsi,
        pk.id_kontak_peserta AS kontak_id, pk.nama_kontak, pk.hub_kontak,
        pk.telp_kontak, pk.email_kontak, pk.alamat_kontak
    FROM 
        tbl_peserta p
    LEFT JOIN peserta_organisasi po ON p.id_peserta = po.id_peserta
    LEFT JOIN peserta_pendidikan pp ON p.id_peserta = pp.id_peserta
    LEFT JOIN peserta_pengalaman pg ON p.id_peserta = pg.id_peserta
    LEFT JOIN peserta_kontak pk ON p.id_peserta = pk.id_peserta
    WHERE p.id_peserta = :idPeserta
    """, nativeQuery = true)
    List<Object[]> findPesertaDetails(@Param("idPeserta") Integer idPeserta);

}


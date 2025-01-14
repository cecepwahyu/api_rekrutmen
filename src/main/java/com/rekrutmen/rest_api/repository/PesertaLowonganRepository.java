package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaLowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PesertaLowonganRepository extends JpaRepository<PesertaLowongan, String> {

    @Query(value = "SELECT lock_status FROM vw_peserta_lock_status WHERE id_peserta = :idPeserta", nativeQuery = true)
    String findLockStatusByIdPeserta(@Param("idPeserta") Integer idPeserta);

    @Query(value = "SELECT * FROM peserta_lowongan WHERE id_lowongan = :idLowongan AND id_peserta = :idPeserta AND is_rekrutmen = :isRekrutmen", nativeQuery = true)
    List<PesertaLowongan> findByLowonganPesertaAndRekrutmen(@Param("idLowongan") Integer idLowongan,
                                                            @Param("idPeserta") Integer idPeserta,
                                                            @Param("isRekrutmen") Boolean isRekrutmen);

}


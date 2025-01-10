package com.rekrutmen.rest_api.repository;

import com.rekrutmen.rest_api.model.PesertaDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PesertaDocumentsRepository extends JpaRepository<PesertaDocuments, Integer> {

    @Query("SELECT d FROM PesertaDocuments d WHERE d.idUser = :idUser AND d.idJenisDokumen = :idJenisDokumen")
    Optional<PesertaDocuments> findByUserIdAndJenisDokumen(@Param("idUser") Integer idUser, @Param("idJenisDokumen") Integer idJenisDokumen);

    @Modifying
    @Query("UPDATE PesertaDocuments d SET d.documentData = :documentData, d.fileName = :fileName, d.fileType = :fileType, d.tanggalUpload = CURRENT_TIMESTAMP, d.documentVersion = d.documentVersion + 1 WHERE d.idUser = :idUser AND d.idJenisDokumen = :idJenisDokumen")
    void updateDocument(
            @Param("idUser") Integer idUser,
            @Param("idJenisDokumen") Integer idJenisDokumen,
            @Param("documentData") String documentData,
            @Param("fileName") String fileName,
            @Param("fileType") String fileType);

    @Query(value = "SELECT nama_dokumen, deskripsi FROM vw_lowongan_dokumen WHERE slug = :slug", nativeQuery = true)
    String findDokumenLowonganBySlug(@Param("slug") String slug);

    @Query("SELECT d.idDocument FROM PesertaDocuments d WHERE d.idUser = :idUser")
    List<Integer> findIdDocumentsByUserId(@Param("idUser") Integer idUser);

    @Modifying
    @Query("DELETE FROM PesertaDocuments d WHERE d.idUser = :idUser AND d.idJenisDokumen = :idJenisDokumen")
    void deleteByUserIdAndJenisDokumen(@Param("idUser") Integer idUser, @Param("idJenisDokumen") Integer idJenisDokumen);

}

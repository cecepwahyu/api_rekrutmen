package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tbl_peserta_documents")
public class PesertaDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document")
    private Integer idDocument;

    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "id_jenis_dokumen")
    private Integer idJenisDokumen;

    @Column(name = "document_data")
    private String documentData;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "tanggal_upload")
    private LocalDateTime tanggalUpload;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "document_version")
    private Integer documentVersion;
}

package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.PesertaDocuments;
import com.rekrutmen.rest_api.repository.PesertaDocumentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PesertaDocumentsService {

    @Autowired
    private PesertaDocumentsRepository documentsRepository;

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateKtp(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenKtp = 1;

        // Check if document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenKtp);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenKtp, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenKtp);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateSkck(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenSkck = 2;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenSkck);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenSkck, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenSkck);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "SKCK document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateToefl(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenToefl = 3;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenToefl);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenToefl, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenToefl);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "TOEFL document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateKartuKeluarga(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenKartuKeluarga = 4;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenKartuKeluarga);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenKartuKeluarga, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenKartuKeluarga);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Kartu Keluarga document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateSuratSehat(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenSuratSehat = 7;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenSuratSehat);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenSuratSehat, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenSuratSehat);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Surat Sehat document submitted successfully",
                null
        ));
    }

    public List<Integer> getIdDocumentsByUserId(Integer idUser) {
        return documentsRepository.findIdDocumentsByUserId(idUser);
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateCv(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenCv = 8;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenCv);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenCv, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenCv);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "CV document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateSuratLamaran(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenSuratLamaran = 9;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenSuratLamaran);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenSuratLamaran, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenSuratLamaran);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Surat Lamaran document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateSuratPernyataan(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenSuratPernyataan = 10;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenSuratPernyataan);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenSuratPernyataan, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenSuratPernyataan);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Surat Pernyataan document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateIjazah(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenIjazah = 11;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenIjazah);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenIjazah, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenIjazah);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Ijazah document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateTranskrip(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenTranskrip = 12;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenTranskrip);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenTranskrip, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenTranskrip);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Transkrip Nilai document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateFotoFullBadan(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenFotoFullBadan = 13;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenFotoFullBadan);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenFotoFullBadan, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenFotoFullBadan);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Foto Full Badan document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdatePasFoto(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenPasFoto = 14;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenPasFoto);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenPasFoto, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenPasFoto);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Pas Foto document submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateDokumenPendukung(
            Integer idPeserta,
            String documentData,
            String fileName,
            String fileType) {

        Integer jenisDokumenDokumenPendukung = 17;

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumenDokumenPendukung);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, jenisDokumenDokumenPendukung, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(jenisDokumenDokumenPendukung);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        return ResponseEntity.ok(new ResponseWrapper<>(
                "000",
                "Dokumen pendukung submitted successfully",
                null
        ));
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> deleteDocument(Integer idPeserta, Integer jenisDokumen) {
        // Check if the document exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, jenisDokumen);

        if (existingDocument.isPresent()) {
            // Delete the document
            documentsRepository.deleteByUserIdAndJenisDokumen(idPeserta, jenisDokumen);

            return ResponseEntity.ok(new ResponseWrapper<>(
                    "000",
                    "Document deleted successfully",
                    null
            ));
        } else {
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    "404",
                    "Document not found",
                    null
            ));
        }
    }

}


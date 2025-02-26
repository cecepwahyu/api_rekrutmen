package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.JenisDokumen;
import com.rekrutmen.rest_api.model.PesertaDocuments;
import com.rekrutmen.rest_api.repository.JenisDokumenRepository;
import com.rekrutmen.rest_api.repository.PesertaDocumentsRepository;
import com.rekrutmen.rest_api.util.LoggerUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PesertaDocumentsService {

    private static final Logger logger = LoggerFactory.getLogger(PesertaDocumentsService.class);

    @Autowired
    private PesertaDocumentsRepository documentsRepository;

    @Autowired
    private JenisDokumenRepository jenisDokumenRepository;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public List<Integer> getIdDocumentsByUserId(Integer idUser) {
        return documentsRepository.findIdDocumentsByUserId(idUser);
    }

    @Transactional
    public ResponseEntity<ResponseWrapper<Object>> submitOrUpdateDocument(
            String processName,
            String token,
            Integer idPeserta,
            Integer idDokumen,
            String documentData,
            String fileName,
            String fileType,
            String request,
            HttpServletRequest httpRequest) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(httpRequest);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                LoggerUtil.convertObjectToMap(request)
        ));

        // Validate if id_dokumen exists in tbl_jenis_dokumen
        Optional<JenisDokumen> jenisDokumenOptional = jenisDokumenRepository.findById(idDokumen);
        if (jenisDokumenOptional.isEmpty()) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("400"),
                    responseCodeUtil.getMessage("400"),
                    Map.of("message", "Invalid idDokumen: {}" + idDokumen)
            ));
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("400"),
                    "Invalid document type",
                    null
            ));
        }

        // Check if the document already exists
        Optional<PesertaDocuments> existingDocument = documentsRepository.findByUserIdAndJenisDokumen(idPeserta, idDokumen);

        if (existingDocument.isPresent()) {
            // Update existing document
            documentsRepository.updateDocument(idPeserta, idDokumen, documentData, fileName, fileType);
        } else {
            // Insert new document
            PesertaDocuments newDocument = new PesertaDocuments();
            newDocument.setIdUser(idPeserta);
            newDocument.setIdJenisDokumen(idDokumen);
            newDocument.setDocumentData(documentData);
            newDocument.setFileName(fileName);
            newDocument.setFileType(fileType);
            newDocument.setTanggalUpload(LocalDateTime.now());
            newDocument.setDocumentVersion(1);
            newDocument.setIsActive(true);

            documentsRepository.save(newDocument);
        }

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "R", processName,
                new ResponseWrapper<>(
                        responseCodeUtil.getCode("000"),
                        "Document submitted successfully",
                        null
                )
        ));
        return ResponseEntity.ok(new ResponseWrapper<>(
                responseCodeUtil.getCode("000"),
                "Document submitted successfully",
                null
        ));
    }

}


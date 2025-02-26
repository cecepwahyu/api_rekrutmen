package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.dto.ResponseWrapper;
import com.rekrutmen.rest_api.model.Referensi;
import com.rekrutmen.rest_api.repository.ReferensiRepository;
import com.rekrutmen.rest_api.util.LoggerUtil;
import com.rekrutmen.rest_api.util.ResponseCodeUtil;
import com.rekrutmen.rest_api.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReferensiService {

    private static final Logger logger = LoggerFactory.getLogger(ReferensiService.class);

    @Autowired
    private ReferensiRepository referensiRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ResponseCodeUtil responseCodeUtil;

    public ResponseEntity<ResponseWrapper<List<Referensi>>> getReferensiByGroup(String processName, String token, String refGroup1, HttpServletRequest request) {
        String jobId = LoggerUtil.getJobId();
        String ip = LoggerUtil.getUserIp(request);

        logger.info(LoggerUtil.formatLog(
                jobId, ip, "S", processName,
                Map.of("token", token)
        ));

        // Validate token
        if (!tokenUtil.isValidToken(token)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    Map.of("message", "Invalid token")
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("299"),
                    responseCodeUtil.getMessage("299"),
                    null
            ));
        }

        // Validate if token is expired
        if (tokenUtil.isTokenExpired(token)) {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    Map.of("message", "Expired token")
            ));
            return ResponseEntity.status(401).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("298"),
                    responseCodeUtil.getMessage("298"),
                    null
            ));
        }

        List<Referensi> referensiList = referensiRepository.findByGroup1(refGroup1);

        if (!referensiList.isEmpty()) {
            logger.info(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    new ResponseWrapper<>(
                            responseCodeUtil.getCode("000"),
                            responseCodeUtil.getMessage("000"),
                            referensiList
                    )
            ));
            return ResponseEntity.ok(new ResponseWrapper<>(
                    responseCodeUtil.getCode("000"),
                    responseCodeUtil.getMessage("000"),
                    referensiList
            ));
        } else {
            logger.warn(LoggerUtil.formatLog(
                    jobId, ip, "R", processName,
                    responseCodeUtil.getCode("404"),
                    responseCodeUtil.getMessage("404"),
                    Map.of("message", "No references found for the specified group")
            ));
            return ResponseEntity.status(404).body(new ResponseWrapper<>(
                    responseCodeUtil.getCode("404"),
                    "No references found for the specified group",
                    null
            ));
        }
    }
}

package com.rekrutmen.rest_api.service;

import com.rekrutmen.rest_api.model.LogWs;
import com.rekrutmen.rest_api.repository.LogWsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final LogWsRepository logWsRepository;

    @Autowired
    public LogService(LogWsRepository logWsRepository) {
        this.logWsRepository = logWsRepository;
    }

    public void log(String jobId, String ip, String signal, String device,
                    String processName, String message, String username, Integer sequence) {
        LogWs logWs = new LogWs(jobId, ip, signal, device, processName, message, username, sequence);
        logWsRepository.save(logWs);
    }
}

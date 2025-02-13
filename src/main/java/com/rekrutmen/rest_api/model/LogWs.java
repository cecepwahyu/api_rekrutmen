package com.rekrutmen.rest_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_ws")
public class LogWs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "log_time", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime logTime;

    @Column(name = "job_id", length = 20)
    private String jobId;

    @Column(name = "ip", length = 20)
    private String ip;

    @Column(name = "signal", length = 1)
    private String signal;

    @Column(name = "device", length = 1)
    private String device;

    @Column(name = "process_name", length = 50)
    private String processName;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "username", length = 25)
    private String username;

    @Column(name = "sequence")
    private Integer sequence;

    // Constructors
    public LogWs() {}

    public LogWs(String jobId, String ip, String signal, String device,
                 String processName, String message, String username, Integer sequence) {
        this.jobId = jobId;
        this.ip = ip;
        this.signal = signal;
        this.device = device;
        this.processName = processName;
        this.message = message;
        this.username = username;
        this.sequence = sequence;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getLogTime() { return logTime; }
    public void setLogTime(LocalDateTime logTime) { this.logTime = logTime; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getSignal() { return signal; }
    public void setSignal(String signal) { this.signal = signal; }

    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }

    public String getProcessName() { return processName; }
    public void setProcessName(String processName) { this.processName = processName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getSequence() { return sequence; }
    public void setSequence(Integer sequence) { this.sequence = sequence; }
}

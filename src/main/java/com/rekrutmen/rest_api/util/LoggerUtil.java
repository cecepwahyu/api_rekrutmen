package com.rekrutmen.rest_api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LoggerUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, AtomicInteger> sequenceMap = new ConcurrentHashMap<>();
    private static final String LOG_DIRECTORY = System.getProperty("user.dir") + "/logs/";

    // SFTP Credentials
    private static final String SFTP_HOST = "192.168.4.79";
    private static final int SFTP_PORT = 22;
    private static final String SFTP_USER = "devftp";
    private static final String SFTP_PASSWORD = "devftp";
    private static final String SFTP_REMOTE_DIR = "/home/devftp/data/api-rekrutmen/logs/";

    public static String getJobId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        return LocalDateTime.now().format(formatter);
    }

    public static int getSequence(String jobId) {
        return sequenceMap.computeIfAbsent(jobId, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * Retrieves the real IP address of the user from the HttpServletRequest.
     *
     * @param request HttpServletRequest object
     * @return String representing the user's IP address
     */
    public static String getUserIp(HttpServletRequest request) {
        if (request == null) {
            return "UNKNOWN";
        }

        // Check for IP from reverse proxies or load balancers
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // If multiple IPs exist (e.g., from proxies), take the first valid IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    private static String getLogFilePath() {
        String logFileName = "log-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log";
        return LOG_DIRECTORY + logFileName;
    }

    private static void writeLogToFile(String logMessage) {
        String filePath = getLogFilePath();
        try {
            Files.createDirectories(Paths.get(LOG_DIRECTORY)); // Ensure directory exists
            try (FileWriter writer = new FileWriter(filePath, true)) {
                writer.write(logMessage + System.lineSeparator());
            }
            //uploadLogToSFTP(filePath);
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }

    /**
     * Uploads the log file to the SFTP server.
     */
    private static void uploadLogToSFTP(String localFilePath) {
        Session session = null;
        ChannelSftp sftpChannel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;

            // Ensure the remote directory exists
            try {
                sftpChannel.cd(SFTP_REMOTE_DIR);
            } catch (SftpException e) {
                sftpChannel.mkdir(SFTP_REMOTE_DIR);
                sftpChannel.cd(SFTP_REMOTE_DIR);
            }

            // Upload file
            File localFile = new File(localFilePath);
            try (InputStream inputStream = new FileInputStream(localFile)) {
                sftpChannel.put(inputStream, localFile.getName(), ChannelSftp.OVERWRITE);
                System.out.println("✅ Log file uploaded to SFTP: " + localFile.getName());
            }
        } catch (JSchException | SftpException | IOException e) {
            System.err.println("❌ SFTP Upload Failed: " + e.getMessage());
        } finally {
            if (sftpChannel != null) {
                sftpChannel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    /**
     * Formats log messages for request payloads (no response code).
     *
     * @param jobId  Unique job ID
     * @param ip     User's IP address
     * @param signal Log signal type ("S" for send, "R" for response)
     * @param data   Request payload
     * @return Formatted log string
     */
    public static String formatLog(String jobId, String ip, String signal, String processName, Map<String, Object> data) {
        try {
            int sequence = getSequence(jobId);
            String jsonData = objectMapper.writeValueAsString(data);
            String logMessage = String.format(
                    "sequence:%-5d job_id:%-20s ip:%-20s signal:%-2s processName:%-20s data:%s",
                    sequence, jobId, ip, signal, processName, jsonData
            );
            writeLogToFile(logMessage);
            return logMessage;
        } catch (Exception e) {
            return String.format(
                    "sequence:%-5d job_id:%-20s ip:%-20s signal:%-2s processName:%-20s data:{ \"error\": \"Failed to generate log JSON\" }",
                    getSequence(jobId), jobId, ip, signal, processName
            );
        }
    }

    /**
     * Formats log messages into the required structure for response payload.
     *
     * @param jobId        Unique job ID
     * @param ip           User's IP address
     * @param signal       Log signal type ("S" for send, "R" for response)
     * @param responseCode Response code
     * @param responseMsg  Response message
     * @param data         Response data
     * @return Formatted log string
     */
    public static String formatLog(String jobId, String ip, String signal, String processName, String responseCode, String responseMsg, Map<String, Object> data) {
        try {
            int sequence = getSequence(jobId);
            String jsonData = objectMapper.writeValueAsString(Map.of(
                    "responseCode", responseCode,
                    "responseMessage", responseMsg,
                    "data", data
            ));
            String logMessage = String.format(
                    "sequence:%-5d job_id:%-20s ip:%-20s signal:%-2s processName:%-20s data:%s",
                    sequence, jobId, ip, signal, processName, jsonData
            );
            writeLogToFile(logMessage);
            return logMessage;
        } catch (Exception e) {
            return String.format(
                    "sequence:%-5d job_id:%-20s ip:%-20s signal:%-2s processName:%-20s data:{ \"error\": \"Failed to generate log JSON\" }",
                    getSequence(jobId), jobId, ip, signal, processName
            );
        }
    }

    /**
     * Converts any object to a JSON string.
     * @param object The object to convert.
     * @return JSON string representation of the object.
     */
    public static String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return "{ \"error\": \"Failed to convert object to JSON\" }";
        }
    }

    /**
     * Converts an object to a Map<String, Object> for flexible logging.
     * @param object The object to convert.
     * @return Map<String, Object> representation of the object.
     */
    public static Map<String, Object> convertObjectToMap(Object object) {
        try {
            return objectMapper.convertValue(object, Map.class);
        } catch (Exception e) {
            return Map.of("error", "Failed to convert object to Map");
        }
    }

}

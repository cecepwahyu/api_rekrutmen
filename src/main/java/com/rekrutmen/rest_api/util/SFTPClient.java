package com.rekrutmen.rest_api.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SFTPClient implements AutoCloseable {
    private Session session;
    private ChannelSftp channelSftp;

    public SFTPClient(String host, String username, String password) throws Exception {
        JSch jsch = new JSch();
        session = jsch.getSession(username, host, 22);
        session.setPassword(password);

        // Disable host key checking for simplicity
        session.setConfig("StrictHostKeyChecking", "no");

        session.connect();
        channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
    }

    public byte[] downloadFile(String remoteFilePath) throws Exception {
        try (InputStream inputStream = channelSftp.get(remoteFilePath);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }

    @Override
    public void close() throws Exception {
        if (channelSftp != null) channelSftp.disconnect();
        if (session != null) session.disconnect();
    }
}


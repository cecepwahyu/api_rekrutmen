package com.rekrutmen.rest_api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${spring.mail.username}") // Email sender (from application.properties)
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Kode OTP");
            helper.setFrom(fromEmail);
            helper.setText(getOtpEmailTemplate(otpCode), true); // Send as HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendOtpEmailVerification(String toEmail, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Kode OTP Verifikasi Akun");
            helper.setFrom(fromEmail);
            helper.setText(getOtpVerificationTemplate(otpCode), true); // Send as HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getEmailHeader() {
        return """
        <html>
        <body style='margin:0;padding:0;background-color:#f5f5f5;font-family:Arial,sans-serif;line-height:1.6;color:#333333;'>
            <div style='max-width:600px;margin:0 auto;background-color:#ffffff;box-shadow:0 1px 3px rgba(0,0,0,0.1);'>
                <div style='background:#005099;color:white;padding:20px;text-align:center;'>
                    <img src='https://i.imgur.com/wTJVk8g.png' alt='BPD DIY Logo' style='max-width:250px;margin:0 auto 15px;display:block;'>
                    <h2 style='margin:0;font-size:24px;'>PT Bank BPD DIY</h2>
                </div>
                <div style='padding:30px;background:#ffffff;'>
        """;
    }

    private String getEmailFooter() {
        return String.format("""
                </div>
                <div style='background:#003366;color:#ffffff;padding:20px;font-size:12px;text-align:center;'>
                    <p style='margin:0;font-weight:bold;'>PT BANK BPD DIY</p>
                    <p style='margin:5px 0;font-size:11px;'>Kantor Pusat: Jl. Tentara Pelajar No. 7 Yogyakarta 55231</p>
                    <div style='margin-top:15px;padding-top:15px;border-top:1px solid rgba(255,255,255,0.2);'>
                        <p style='margin:5px 0;'>
                            Contact Center: (0274) 561614<br>
                            Email: <a href='mailto:pemberdayaan.sdm@bpddiy.co.id' style='color:#ffffff;text-decoration:none;'>pemberdayaan.sdm@bpddiy.co.id</a><br>
                            Website: <a href='https://bpddiy.co.id/' style='color:#ffffff;text-decoration:none;'>https://bpddiy.co.id/</a>
                        </p>
                    </div>
                    <p style='margin-top:20px;font-size:10px;color:#cccccc;'>
                        Email ini dikirim secara otomatis. Mohon tidak membalas email ini.<br>
                        &copy; %d PT BANK BPD DIY. All Rights Reserved.
                    </p>
                </div>
            </div>
        </body>
        </html>
        """, Year.now().getValue());
    }

    private String getOtpEmailTemplate(String otpCode) {
        return String.format("""
                %s
                <p>Yth. Peserta,</p>
                <p>Anda telah mengajukan permintaan untuk mereset kata sandi akun Anda. Gunakan kode OTP berikut untuk melanjutkan proses reset kata sandi:</p>
                <div style='background:#f7f7f7;padding:15px;text-align:center;font-size:20px;font-weight:bold;color:#005099;border-radius:5px;'>
                    %s
                </div>
                <p>Silakan klik tombol di bawah ini untuk melanjutkan proses reset password:</p>
                <div style='text-align:center;'>
                    <a href='%s/otp' 
                        style='display:inline-block;padding:10px 20px;color:white;background:#005099;text-decoration:none;border-radius:5px;font-weight:bold;'>Reset Password</a>
                </div>
                <p style='font-size:12px;color:#666;'>Jika tombol tidak berfungsi, silakan salin dan tempel URL berikut di browser Anda: %s/otp</p>
                %s
        """, getEmailHeader(), otpCode, baseUrl, baseUrl, getEmailFooter());
    }

    private String getOtpVerificationTemplate(String otpCode) {
        return String.format("""
                %s
                <p>Yth. Peserta,</p>
                <p>Terima kasih telah mendaftar di PT Bank BPD DIY. Untuk menyelesaikan proses pendaftaran, gunakan kode OTP berikut untuk verifikasi akun Anda:</p>
                <div style='background:#f7f7f7;padding:15px;text-align:center;font-size:20px;font-weight:bold;color:#005099;border-radius:5px;'>
                    %s
                </div>
                <p>Silakan klik tombol di bawah ini untuk memverifikasi akun Anda:</p>
                <div style='text-align:center;'>
                    <a href='%s/account-verification' 
                        style='display:inline-block;padding:10px 20px;color:white;background:#005099;text-decoration:none;border-radius:5px;font-weight:bold;'>Verifikasi Akun</a>
                </div>
                <p style='font-size:12px;color:#666;'>Jika tombol tidak berfungsi, silakan salin dan tempel URL berikut di browser Anda: %s/account-verification</p>
                %s
        """, getEmailHeader(), otpCode, baseUrl, baseUrl, getEmailFooter());
    }
}

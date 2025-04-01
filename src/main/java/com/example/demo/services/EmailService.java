package com.example.demo.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @Value("${spring.mail.username}")
    private String email;
    public void sendEmail(String recipient, String subject, String text) throws MessagingException {
//Todo cần cấu hình phần này lai để gửi mail
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(email);
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(text, true);
        javaMailSender.send(message);
        System.out.println(String.format("Sending email to: %s\nSubject: %s\nText: %s", recipient, subject, text));
    }
}

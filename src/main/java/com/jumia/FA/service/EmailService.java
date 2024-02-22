package com.jumia.FA.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;

    }


    public void LoginNotification(String userEmail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(userEmail);
        mimeMessageHelper.setSubject("Login Alert");
        mimeMessageHelper.setText("You've successfully logged in. If this wasn't you, please contact support.");
        mailSender.send(mimeMessage);
    }
}

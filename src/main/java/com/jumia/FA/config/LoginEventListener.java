package com.jumia.FA.config;

import com.jumia.FA.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class LoginEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final EmailService emailService;
    
    public LoginEventListener(EmailService emailService){
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        // Extract user information from the authentication object
        String userEmail = authentication.getName();

        try {
            emailService.LoginNotification(userEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    }


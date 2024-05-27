package com.example.demo.service.email_service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class EmailServiceTest {


    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void sentEmail_Success() {
        String to = "abdulaziz2002ece@gmail.com";
        String subject = "Test Email";
        String body = "Test Email";

        emailService.sentEmail(to,subject,body);

        verify(mailSender,times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sentEmail_Failure() {

        String to = "2nv@alid@gmail.com";
        String subject = "Test Email";
        String body = "Test Email";

        assertThrows(IllegalArgumentException.class,() -> emailService.sentEmail(to,subject,body));

        verify(mailSender,never()).send(any(SimpleMailMessage.class));
    }
}
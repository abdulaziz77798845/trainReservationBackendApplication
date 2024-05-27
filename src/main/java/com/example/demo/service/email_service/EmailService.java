package com.example.demo.service.email_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sentEmail(String to,String subject,String body){


        if (!isValidEmail(to)){
            throw new IllegalArgumentException("Invalid email address: "+to);
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("azizatwork3@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);

        mailSender.send(mailMessage);
    }

    private boolean isValidEmail(String email) {
        return email!=null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
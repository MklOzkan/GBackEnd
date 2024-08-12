package com.project.service.user;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService implements EmailServiceInterface {

    private final JavaMailSender javaMailSender;




    public void sendEmail(MimeMessagePreparator mimeMessagePreparator) {
        javaMailSender.send(mimeMessagePreparator);
    }
}




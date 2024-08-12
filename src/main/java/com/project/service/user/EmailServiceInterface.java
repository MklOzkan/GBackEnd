package com.project.service.user;

import org.springframework.mail.javamail.MimeMessagePreparator;

public interface EmailServiceInterface {

    void sendEmail(MimeMessagePreparator mimeMessagePreparator);
}

package com.emtech.ushurusmart.utils;


import com.emtech.ushurusmart.config.LoggerSingleton;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService extends LoggerSingleton {
    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message
            helper.setFrom("samuelmaynaw@gmail.com"); // Use the email configured in spring.mail.username
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true indicates the text is HTML
            emailSender.send(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}

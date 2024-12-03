package com.tandem.service;

import com.tandem.model.MailMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailService implements IMailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Value("${smtp.user-name}")
    private String userName;

    @Value("${smtp.user-password}")
    private String userPassword;

    private Session session;
    private final Properties properties = new Properties();

    @PostConstruct
    public void init() {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, userPassword);
            }
        });

        logger.info("MailService initialized with user: {}", userName);
    }

    @Override
    public void sendMail(MailMessage message) {
        if (message == null) {
            logger.error("MailService: sendMail - message can't be null");
            return;
        }

        if (!isMail(message.to())) {
            logger.error("MailService: sendMail - Invalid Email Address: {}", message.to());
            return;
        }

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(userName);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(message.to()));
            mimeMessage.setSubject(message.subject());
            mimeMessage.setText(message.message());

            Transport.send(mimeMessage);
            logger.info("Mail sent successfully to {}", message.to());
        } catch (MessagingException e) {
            logger.error("MailService: sendMail - Error sending email to {}: {}", message.to(), e.getMessage());
        }
    }

    private boolean isMail(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return s.matches(emailRegex);
    }
}

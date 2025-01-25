package com.tandem.service.mail;

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

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private Properties properties;

    @PostConstruct
    public void init() {
        properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
    }

    @Override
    public void sendMail(String email, String verifyCode) {
        logger.info("Preparing to send verification email to: {}", email);

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Tandem Verification Code");

            String htmlContent = getHtmlContent(verifyCode);

            message.setContent(htmlContent, "text/html");
            Transport.send(message);

            logger.info("Verification email sent successfully to: {}", email);
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}. Error: {}", email, e.getMessage());
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }
    }

    private String getHtmlContent(String verifyCode) {
        return new StringBuilder()
                .append("""
            <html>
                <body style="background-color: #8379e7; color: #ecf0f1; font-family: Arial, sans-serif; text-align: center; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background-color: #766ce9; border-radius: 10px; padding: 20px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
                        <h1 style="color: #fff; font-size: 32px; margin-bottom: 20px;">Verification Code</h1>
                        <p style="font-size: 24px; font-weight: bold; margin: 20px 0;">""")
                .append(verifyCode)
                .append("""
                        </p>
                        <p style="font-size: 16px; color: #bdc3c7;">Please use this code to complete your verification process. This code is valid for a limited time only.</p>
                        <hr style="border: none; border-top: 1px solid #7f8c8d; margin: 20px 0;">
                        <p style="font-size: 14px; color: #95a5a6;">This message was generated automatically. Please do not reply.</p>
                    </div>
                </body>
            </html>
            """)
                .toString();
    }

}

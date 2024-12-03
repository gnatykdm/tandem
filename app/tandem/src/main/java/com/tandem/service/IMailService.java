package com.tandem.service;

import com.tandem.model.MailMessage;
import org.springframework.stereotype.Service;

@Service
public interface IMailService {
    void sendMail(MailMessage message);
}

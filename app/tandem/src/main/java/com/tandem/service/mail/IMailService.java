package com.tandem.service.mail;

import org.springframework.stereotype.Service;

@Service
public interface IMailService {
    void sendMail(String email, String verifyCode);
}

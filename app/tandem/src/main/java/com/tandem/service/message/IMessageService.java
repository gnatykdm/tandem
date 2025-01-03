package com.tandem.service.message;

import com.tandem.model.entity.MessageEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public interface IMessageService {
    void addMessage(Long sender, String content, LocalDateTime sendAt);
    void updateMessage(Long messageId, String content, LocalDateTime sendAt);
    List<Object[]> getMessageById(Long messageId);
    void deleteMessage(Long messageId);
    List<MessageEntity> getAllMessagesSentByUser(Long userId);
}

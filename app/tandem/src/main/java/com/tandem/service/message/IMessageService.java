package com.tandem.service.message;

import com.tandem.model.entity.MessageEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public interface IMessageService {
    void addMessage(Long senderId, String content, Long groupId, LocalDateTime sendAt);
    List<Object[]> getAllMessagesFromGroup(Long groupId);
}

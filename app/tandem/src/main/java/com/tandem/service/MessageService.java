package com.tandem.service;

import com.tandem.model.dto.MessageDTO;
import com.tandem.model.entity.MessageEntity;
import com.tandem.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    public void addMessage(MessageDTO message) {
        if (message == null) {
            logger.error("Message saving: Message DTO can't be empty");
            throw new IllegalArgumentException("Message cannot be null");
        }
        messageRepository.save(messageMapper(message));
        logger.info("Message saved successfully");
    }

    public void updateMessage(MessageDTO message) {
        if (message == null) {
            logger.error("Message updating: Message DTO can't be empty");
            throw new IllegalArgumentException("Message cannot be null");
        }
        messageRepository.save(messageMapper(message));
        logger.info("Message updated successfully");
    }

    public void dropMessageById(Long id) {
        if (id == null || id < 0) {
            logger.error("Message deletion: Message Id can't be less than zero");
            throw new IllegalArgumentException("Invalid message ID");
        }
        if (!messageRepository.existsById(id)) {
            logger.error("Message deletion: Message not found with id {}", id);
            throw new RuntimeException("Message not found");
        }
        messageRepository.deleteById(id);
        logger.info("Message deleted successfully with id: {}", id);
    }

    public MessageEntity findMessageById(Long id) {
        if (id == null || id < 0) {
            logger.error("Message find: Message Id can't be less than zero");
            throw new IllegalArgumentException("Invalid message ID");
        }
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    public List<MessageEntity> findByGroupId(Long groupId) {
        return messageRepository.findByGroupId(groupId);
    }

    public List<MessageEntity> findBySenderId(Long senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    private MessageEntity messageMapper(MessageDTO dto) {
        return new MessageEntity(
                dto.getSender(),
                dto.getContent()
        );
    }
}

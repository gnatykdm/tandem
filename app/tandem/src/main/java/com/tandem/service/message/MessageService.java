package com.tandem.service.message;

import com.tandem.model.entity.MessageEntity;
import com.tandem.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService implements IMessageService {

    @Autowired
    private MessageRepository messageRepository;
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Override
    public void addMessage(Long sender, String content, LocalDateTime sendAt) {
        if (sender == null || sender <= 0 || content == null || content.isEmpty()) {
            logger.error("Message Service - addMessage: Invalid sender or content");
            throw new IllegalArgumentException("Sender and content cannot be null or empty");
        }

        logger.info("Message Service - addMessage: Adding a new message");
        messageRepository.addMessage(sender, content, sendAt);
    }

    @Override
    public void updateMessage(Long messageId, String content, LocalDateTime sendAt) {
        if (messageId == null || messageId <= 0 || content == null || content.isEmpty()) {
            logger.error("Message Service - updateMessage: Invalid messageId or content");
            throw new IllegalArgumentException("Message ID and content cannot be null or empty");
        }

        logger.info("Message Service - updateMessage: Updating message with ID: {}", messageId);
        messageRepository.updateMessage(messageId, content, sendAt);
    }

    @Override
    public List<Object[]> getMessageById(Long messageId) {
        if (messageId == null || messageId <= 0) {
            logger.error("Message Service - getMessageById: Invalid messageId");
            throw new IllegalArgumentException("Message ID cannot be null or less than or equal to 0");
        }

        logger.info("Message Service - getMessageById: Retrieving message with ID: {}", messageId);
        return messageRepository.getMessageById(messageId);
    }

    @Override
    public void deleteMessage(Long messageId) {
        if (messageId == null || messageId <= 0) {
            logger.error("Message Service - deleteMessage: Invalid messageId");
            throw new IllegalArgumentException("Message ID cannot be null or less than or equal to 0");
        }

        logger.info("Message Service - deleteMessage: Deleting message with ID: {}", messageId);
        messageRepository.deleteMessage(messageId);
    }

    @Override
    public List<MessageEntity> getAllMessagesSentByUser(Long userId) {
        if (userId == null || userId <= 0) {
            logger.error("Message Service - getAllMessagesSentByUser: Invalid userId");
            throw new IllegalArgumentException("User ID cannot be null or less than or equal to 0");
        }

        logger.info("Message Service - getAllMessagesSentByUser: Retrieving messages sent by user ID: {}", userId);
        return messageRepository.getAllMessageSentByUser(userId);
    }
}

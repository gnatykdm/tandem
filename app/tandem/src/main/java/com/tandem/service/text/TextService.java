package com.tandem.service.text;

import com.tandem.model.entity.TextEntity;
import com.tandem.repository.TextRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextService implements ITextService {

    @Autowired
    private TextRepository textRepository;
    private final Logger logger = LoggerFactory.getLogger(TextService.class);

    @Override
    public void saveText(TextEntity text) {
        try {
            logger.info("Saving text: {}", text);
            textRepository.save(text);
        } catch (Exception e) {
            logger.error("Error while saving text: {}", text, e);
            throw new RuntimeException("Error while saving text", e);
        }
    }

    @Override
    public void deleteTextById(Long textId) {
        try {
            logger.info("Deleting text with ID: {}", textId);
            textRepository.deleteById(textId);
        } catch (Exception e) {
            logger.error("Error while deleting text with ID: {}", textId, e);
            throw new RuntimeException("Error while deleting text", e);
        }
    }

    @Override
    public List<TextEntity> getAllTexts() {
        logger.info("Fetching all texts.");
        return textRepository.findAll();
    }

    @Override
    public List<TextEntity> getAllTextsSentByUser(Long userId) {
        logger.info("Fetching all texts sent by user with ID: {}", userId);
        return textRepository.getAllTextByUser(userId);
    }
}

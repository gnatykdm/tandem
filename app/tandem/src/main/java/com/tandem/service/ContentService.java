package com.tandem.service;

import com.tandem.repository.ContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    private Logger logger = LoggerFactory.getLogger(ContentService.class);

    public Long addContent(Long photoId, Long videoId, Long audioId) {
        try {
            logger.info("Adding content with photoId: {}, videoId: {}, audioId: {}", photoId, videoId, audioId);
            Long contentId = contentRepository.addContent(photoId, videoId, audioId);

            logger.info("Content added successfully with ID: {}", contentId);
            return contentId;
        } catch (Exception e) {
            logger.error("Error adding content: {}", e.getMessage());
            throw new RuntimeException("Error adding content", e);
        }
    }
}

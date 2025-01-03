package com.tandem.service.content;

import com.tandem.model.entity.ContentEntity;
import com.tandem.repository.ContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContentService implements IContentService {

    @Autowired
    private ContentRepository contentRepository;
    private final Logger logger = LoggerFactory.getLogger(ContentService.class);

    @Transactional
    @Override
    public void saveContent(Long photoId, Long videoId, Long audioId, Long textId) {
        logger.info("Content Service - saveContent: Saving Content");
        contentRepository.addContent(photoId, videoId, audioId, textId);
    }

    @Transactional
    @Override
    public void deleteContent(Long contentId) {
        if (contentId == null || contentId <= 0) {
            logger.error("Content Service - deleteContent: Invalid Content ID provided");
            throw new IllegalArgumentException("Content ID must be positive");
        }

        logger.info("Content Service - deleteContent: Deleting content");
        boolean canDelete = checkContentDeletable(contentId);
        if (!canDelete) {
            logger.error("Content Service - deleteContent: Content cannot be deleted due to existing dependencies");
            throw new IllegalStateException("Content cannot be deleted due to dependencies");
        }

        contentRepository.deleteContent(contentId);
    }

    @Override
    public boolean checkContentDeletable(Long contentId) {
        if (contentId == null || contentId <= 0) {
            logger.error("Content Service - checkContentDeletable: Invalid Content ID provided");
            throw new IllegalArgumentException("Content ID must be positive");
        }

        logger.info("Content Service - checkContentDeletable: Checking if content can be deleted");
        return contentRepository.checkContentDeletable(contentId);
    }

    @Override
    public List<ContentEntity> getAllContents() {
        logger.info("Content Service - getAllContents: Getting all content");
        return contentRepository.findAll();
    }
}

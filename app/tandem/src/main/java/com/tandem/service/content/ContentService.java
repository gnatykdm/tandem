package com.tandem.service.content;

import com.tandem.repository.ContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContentService implements IContentService {

    @Autowired
    private ContentRepository contentRepository;

    private final Logger logger = LoggerFactory.getLogger(ContentService.class);

    @Override
    @Transactional
    public void addContent(Long userId, Long photoId, Long videoId, Long audioId, Long textId) {
        try {
            logger.info("Adding content for user {} with photoId {}, videoId {}, audioId {}, textId {}",
                    userId, photoId, videoId, audioId, textId);
            contentRepository.addContent(userId, photoId, videoId, audioId, textId);
            logger.info("Content added successfully for user {}", userId);
        } catch (Exception e) {
            logger.error("Error adding content for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteContent(Long contentId) {
        try {
            logger.info("Deleting content with contentId {}", contentId);
            contentRepository.deleteContent(contentId);
            logger.info("Content deleted successfully with contentId {}", contentId);
        } catch (Exception e) {
            logger.error("Error deleting content with contentId {}: {}", contentId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getContentByUser(Long userId) {
        try {
            logger.info("Fetching content for user {}", userId);
            List<Object[]> content = contentRepository.getContentByUser(userId);
            logger.info("Fetched {} content items for user {}", content.size(), userId);
            return content;
        } catch (Exception e) {
            logger.error("Error fetching content for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void createPhoto(String photoUrl, String description, Long userId) {
        try {
            logger.info("Creating photo with URL {} and description {} for user {}", photoUrl, description, userId);
            contentRepository.createPhoto(photoUrl, description, userId);
            logger.info("Photo created successfully for user {}", userId);
        } catch (Exception e) {
            logger.error("Error creating photo for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getPhotoById(Long photoId) {
        try {
            logger.info("Fetching photo with photoId {}", photoId);
            List<Object[]> photo = contentRepository.getPhotoById(photoId);
            logger.info("Fetched photo for photoId {}", photoId);
            return photo;
        } catch (Exception e) {
            logger.error("Error fetching photo with photoId {}: {}", photoId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void deletePhoto(Long photoId) {
        try {
            logger.info("Attempting to delete photo with photoId {}", photoId);
            contentRepository.deletePhoto(photoId);
            logger.info("Photo deleted successfully with photoId {}", photoId);
        } catch (DataAccessException e) {
            logger.error("Database error occurred while deleting photo with photoId {}: {}", photoId, e.getMessage());
            throw new IllegalArgumentException("Photo with ID " + photoId + " not found.", e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting photo with photoId {}: {}", photoId, e.getMessage());
            throw new RuntimeException("An unexpected error occurred while deleting the photo.", e);
        }
    }

    @Override
    public List<Object[]> getAllPhotos() {
        try {
            logger.info("Fetching all photos");
            List<Object[]> photos = contentRepository.getAllPhotos();
            logger.info("Fetched {} photos", photos.size());
            return photos;
        } catch (Exception e) {
            logger.error("Error fetching all photos: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getPhotosByUser(Long userId) {
        try {
            logger.info("Fetching photos for user {}", userId);
            List<Object[]> photos = contentRepository.getPhotosByUser(userId);
            logger.info("Fetched {} photos for user {}", photos.size(), userId);
            return photos;
        } catch (Exception e) {
            logger.error("Error fetching photos for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void createVideo(String videoUrl, String description, Long userId) {
        try {
            logger.info("Creating video with URL {} and description {} for user {}", videoUrl, description, userId);
            contentRepository.createVideo(videoUrl, description, userId);
            logger.info("Video created successfully for user {}", userId);
        } catch (Exception e) {
            logger.error("Error creating video for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getVideoById(Long videoId) {
        try {
            logger.info("Fetching video with videoId {}", videoId);
            List<Object[]> video = contentRepository.getVideoById(videoId);
            logger.info("Fetched video for videoId {}", videoId);
            return video;
        } catch (Exception e) {
            logger.error("Error fetching video with videoId {}: {}", videoId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteVideo(Long videoId) {
        try {
            logger.info("Deleting video with videoId {}", videoId);
            contentRepository.deleteVideo(videoId);
            logger.info("Video deleted successfully with videoId {}", videoId);
        } catch (Exception e) {
            logger.error("Error deleting video with videoId {}: {}", videoId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getAllVideos() {
        try {
            logger.info("Fetching all videos");
            List<Object[]> videos = contentRepository.getAllVideos();
            logger.info("Fetched {} videos", videos.size());
            return videos;
        } catch (Exception e) {
            logger.error("Error fetching all videos: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getVideosByUser(Long userId) {
        try {
            logger.info("Fetching videos for user {}", userId);
            List<Object[]> videos = contentRepository.getVideosByUser(userId);
            logger.info("Fetched {} videos for user {}", videos.size(), userId);
            return videos;
        } catch (Exception e) {
            logger.error("Error fetching videos for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void createAudio(String audioUrl, Long userId) {
        try {
            logger.info("Creating audio with URL {} for user {}", audioUrl, userId);
            contentRepository.createAudio(audioUrl, userId);
            logger.info("Audio created successfully for user {}", userId);
        } catch (Exception e) {
            logger.error("Error creating audio for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getAudioById(Long audioId) {
        try {
            logger.info("Fetching audio with audioId {}", audioId);
            List<Object[]> audio = contentRepository.getAudioById(audioId);
            logger.info("Fetched audio for audioId {}", audioId);
            return audio;
        } catch (Exception e) {
            logger.error("Error fetching audio with audioId {}: {}", audioId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteAudio(Long audioId) {
        try {
            logger.info("Deleting audio with audioId {}", audioId);
            contentRepository.deleteAudio(audioId);
            logger.info("Audio deleted successfully with audioId {}", audioId);
        } catch (Exception e) {
            logger.error("Error deleting audio with audioId {}: {}", audioId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getAudiosByUser(Long userId) {
        try {
            logger.info("Fetching audios for user {}", userId);
            List<Object[]> audios = contentRepository.getAudiosByUser(userId);
            logger.info("Fetched {} audios for user {}", audios.size(), userId);
            return audios;
        } catch (Exception e) {
            logger.error("Error fetching audios for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void createTextContent(String content, Long userId) {
        try {
            logger.info("Creating text content for user {}: {}", userId, content);
            contentRepository.createTextContent(content, userId);
            logger.info("Text content created successfully for user {}", userId);
        } catch (Exception e) {
            logger.error("Error creating text content for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getTextById(Long textId) {
        try {
            logger.info("Fetching text content with textId {}", textId);
            List<Object[]> text = contentRepository.getTextById(textId);
            logger.info("Fetched text content for textId {}", textId);
            return text;
        } catch (Exception e) {
            logger.error("Error fetching text content with textId {}: {}", textId, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteTextContent(Long textId) {
        try {
            logger.info("Deleting text content with textId {}", textId);
            contentRepository.deleteTextContent(textId);
            logger.info("Text content deleted successfully with textId {}", textId);
        } catch (Exception e) {
            logger.error("Error deleting text content with textId {}: {}", textId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getAllTexts() {
        try {
            logger.info("Fetching all text content");
            List<Object[]> texts = contentRepository.getAllTexts();
            logger.info("Fetched {} text content items", texts.size());
            return texts;
        } catch (Exception e) {
            logger.error("Error fetching all text content: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object[]> getTextsByUser(Long userId) {
        try {
            logger.info("Fetching text content for user {}", userId);
            List<Object[]> texts = contentRepository.getTextsByUser(userId);
            logger.info("Fetched {} text content items for user {}", texts.size(), userId);
            return texts;
        } catch (Exception e) {
            logger.error("Error fetching text content for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }
}

package com.tandem.service.video;

import com.tandem.model.entity.VideoEntity;
import com.tandem.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService implements IVideoService {

    @Autowired
    private VideoRepository videoRepository;
    private final Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Override
    public void saveVideo(VideoEntity video) {
        try {
            logger.info("Saving video: {}", video);
            videoRepository.save(video);
        } catch (Exception e) {
            logger.error("Error while saving video: {}", video, e);
            throw new RuntimeException("Error while saving video", e);
        }
    }

    @Override
    public void deleteVideoById(Long id) {
        try {
            logger.info("Deleting video with ID: {}", id);
            videoRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error while deleting video with ID: {}", id, e);
            throw new RuntimeException("Error while deleting video", e);
        }
    }

    @Override
    public List<VideoEntity> getAllVideos() {
        logger.info("Fetching all videos.");
        return videoRepository.findAll();
    }

    @Override
    public List<VideoEntity> getAllVideoSentByUser(Long userId) {
        logger.info("Fetching all videos sent by user with ID: {}", userId);
        return videoRepository.getAllVideosByUser(userId);
    }
}

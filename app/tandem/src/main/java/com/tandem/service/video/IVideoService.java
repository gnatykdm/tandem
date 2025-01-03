package com.tandem.service.video;

import com.tandem.model.entity.VideoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IVideoService {
    void saveVideo(VideoEntity video);
    void deleteVideoById(Long id);
    List<VideoEntity> getAllVideos();
    List<VideoEntity> getAllVideoSentByUser(Long userId);
}

package com.tandem.service.content;

import com.tandem.model.entity.AudioEntity;
import com.tandem.model.entity.PhotoEntity;
import com.tandem.model.entity.VideoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IContentService {
    void addContent(Long userId, Long photoId, Long videoId, Long audioId, Long textId);
    void deleteContent(Long contentId);
    List<Object[]> getContentByUser(Long userId);

    void createPhoto(String photoUrl, String description, Long userId);
    PhotoEntity getPhotoById(Long photoId);
    void deletePhoto(Long photoId);
    List<Object[]> getAllPhotos();
    List<PhotoEntity> getPhotosByUser(Long userId);

    void createVideo(String videoUrl, String description, Long userId);
    VideoEntity getVideoById(Long videoId);
    void deleteVideo(Long videoId);
    List<Object[]> getAllVideos();
    List<VideoEntity> getVideosByUser(Long userId);

    void createAudio(String audioUrl, Long userId);
    AudioEntity getAudioById(Long audioId);
    void deleteAudio(Long audioId);
    List<AudioEntity> getAudiosByUser(Long userId);

    void createTextContent(String content, Long userId);
    List<Object[]> getTextById(Long textId);
    void deleteTextContent(Long textId);
    List<Object[]> getAllTexts();
    List<Object[]> getTextsByUser(Long userId);
}

package com.tandem.repository;

import com.tandem.model.entity.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<ContentEntity, Long> {

    @Transactional
    @Query(value = "CALL content_management.add_content(:userId, :photoId, :videoId, :audioId, :textId)", nativeQuery = true)
    void addContent(
            @Param("userId") Long userId,
            @Param("photoId") Long photoId,
            @Param("videoId") Long videoId,
            @Param("audioId") Long audioId,
            @Param("textId") Long textId
    );

    @Transactional
    @Query(value = "CALL content_management.delete_content(:contentId)", nativeQuery = true)
    void deleteContent(@Param("contentId") Long contentId);

    @Query(value = "SELECT * FROM content_management.get_content_by_user(:userId)", nativeQuery = true)
    List<Object[]> getContentByUser(@Param("userId") Long userId);

    @Transactional
    @Query(value = "CALL content_management.create_photo(:photoUrl, :description, :userId)", nativeQuery = true)
    void createPhoto(@Param("photoUrl") String photoUrl, @Param("description") String description, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM content_management.get_photo_by_id(:photoId)", nativeQuery = true)
    List<Object[]> getPhotoById(@Param("photoId") Long photoId);

    @Transactional
    @Query(value = "CALL content_management.delete_photo(:photoId)", nativeQuery = true)
    void deletePhoto(@Param("photoId") Long photoId);

    @Query(value = "SELECT * FROM content_management.get_all_photos()", nativeQuery = true)
    List<Object[]> getAllPhotos();

    @Query(value = "SELECT * FROM content_management.get_photos_by_user(:userId)", nativeQuery = true)
    List<Object[]> getPhotosByUser(@Param("userId") Long userId);

    // CRUD for Video
    @Transactional
    @Query(value = "CALL content_management.create_video(:videoUrl, :description, :userId)", nativeQuery = true)
    void createVideo(@Param("videoUrl") String videoUrl, @Param("description") String description, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM content_management.get_video_by_id(:videoId)", nativeQuery = true)
    List<Object[]> getVideoById(@Param("videoId") Long videoId);

    @Transactional
    @Query(value = "CALL content_management.delete_video(:videoId)", nativeQuery = true)
    void deleteVideo(@Param("videoId") Long videoId);

    @Query(value = "SELECT * FROM content_management.get_all_videos()", nativeQuery = true)
    List<Object[]> getAllVideos();

    @Query(value = "SELECT * FROM content_management.get_videos_by_user(:userId)", nativeQuery = true)
    List<Object[]> getVideosByUser(@Param("userId") Long userId);

    // CRUD for Audio
    @Transactional
    @Query(value = "CALL content_management.create_audio(:audioUrl, :userId)", nativeQuery = true)
    void createAudio(@Param("audioUrl") String audioUrl, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM content_management.get_audio_by_id(:audioId)", nativeQuery = true)
    List<Object[]> getAudioById(@Param("audioId") Long audioId);

    @Transactional
    @Query(value = "CALL content_management.delete_audio(:audioId)", nativeQuery = true)
    void deleteAudio(@Param("audioId") Long audioId);

    @Query(value = "SELECT * FROM content_management.get_audios_by_user(:userId)", nativeQuery = true)
    List<Object[]> getAudiosByUser(@Param("userId") Long userId);

    @Transactional
    @Query(value = "CALL content_management.create_text_content(:content, :userId)", nativeQuery = true)
    void createTextContent(@Param("content") String content, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM content_management.get_text_by_id(:textId)", nativeQuery = true)
    List<Object[]> getTextById(@Param("textId") Long textId);

    @Transactional
    @Query(value = "CALL content_management.delete_text_content(:textId)", nativeQuery = true)
    void deleteTextContent(@Param("textId") Long textId);

    @Query(value = "SELECT * FROM content_management.get_all_texts()", nativeQuery = true)
    List<Object[]> getAllTexts();

    @Query(value = "SELECT * FROM content_management.get_texts_by_user(:userId)", nativeQuery = true)
    List<Object[]> getTextsByUser(@Param("userId") Long userId);
}

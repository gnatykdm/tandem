package com.tandem.repository;

import com.tandem.model.entity.AudioEntity;
import com.tandem.model.entity.ContentEntity;
import com.tandem.model.entity.PhotoEntity;
import com.tandem.model.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
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

    @Procedure(value = "content_management.create_photo")
    void createPhoto(@Param("p_photo_url") String photoUrl,
                     @Param("p_description") String description,
                     @Param("p_user_id") Long userId);

    @Query("SELECT p FROM PhotoEntity p WHERE p.photoId = :photoId")
    PhotoEntity getPhotoById(@Param("photoId") Long photoId);

    @Modifying
    @Transactional
    @Query(value = "CALL content_management.delete_photo(:photoId)", nativeQuery = true)
    void deletePhoto(@Param("photoId") Long photoId);

    @Query(value = "SELECT * FROM content_management.get_all_photos()", nativeQuery = true)
    List<Object[]> getAllPhotos();

    @Query("SELECT p FROM PhotoEntity p WHERE p.userId = :userId")
    List<PhotoEntity> getPhotosByUser(@Param("userId") Long userId);

    @Procedure(value = "content_management.create_video")
    void createVideo(@Param("p_video_url") String videoUrl,
                     @Param("p_description") String description,
                     @Param("p_user_id") Long userId);

    @Query("SELECT v FROM VideoEntity v WHERE v.videoId = :videoId")
    VideoEntity getVideoById(@Param("videoId") Long videoId);

    @Modifying
    @Transactional
    @Query(value = "CALL content_management.delete_video(:videoId)", nativeQuery = true)
    void deleteVideo(@Param("videoId") Long videoId);

    @Query(value = "SELECT * FROM content_management.get_all_videos()", nativeQuery = true)
    List<Object[]> getAllVideos();

    @Query("SELECT v FROM VideoEntity v WHERE v.userId = :userId")
    List<VideoEntity> getVideosByUser(@Param("userId") Long userId);

    @Procedure(value = "content_management.create_audio")
    void createAudio(@Param("p_audio_url") String audioUrl, @Param("p_user_id") Long userId);

    @Query("SELECT a FROM AudioEntity a WHERE a.audioId = :audioId")
    AudioEntity getAudioById(@Param("audioId") Long audioId);

    @Modifying
    @Transactional
    @Query(value = "CALL content_management.delete_audio(:audioId)", nativeQuery = true)
    void deleteAudio(@Param("audioId") Long audioId);

    @Query("SELECT a FROM AudioEntity a WHERE a.userId = :userId")
    List<AudioEntity> getAudiosByUser(@Param("userId") Long userId);

    @Procedure(value = "content_management.create_text_content")
    void createTextContent(@Param("p_content") String content, @Param("p_user_id") Long userId);

    @Query(value = "SELECT * FROM content_management.get_text_by_id(:textId)", nativeQuery = true)
    List<Object[]> getTextById(@Param("textId") Long textId);

    @Modifying
    @Transactional
    @Query(value = "CALL content_management.delete_text_content(:textId)", nativeQuery = true)
    void deleteTextContent(@Param("textId") Long textId);

    @Query(value = "SELECT * FROM content_management.get_all_texts()", nativeQuery = true)
    List<Object[]> getAllTexts();

    @Query(value = "SELECT * FROM content_management.get_texts_by_user(:userId)", nativeQuery = true)
    List<Object[]> getTextsByUser(@Param("userId") Long userId);
}

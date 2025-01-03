package com.tandem.repository;

import com.tandem.model.entity.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

public interface ContentRepository extends JpaRepository<ContentEntity, Long> {
    @Procedure(name = "content_management.add_content")
    void addContent(@Param("p_photo") Integer photoId, @Param("p_video") Integer videoId,
                    @Param("p_audio") Integer audioId, @Param("p_text") Integer textId);

    @Procedure(name = "content_management.delete_content")
    void deleteContent(@Param("p_content_id") Integer contentId);

    @Query(value = "SELECT content_management.check_content_deletable(:p_content_id)", nativeQuery = true)
    boolean checkContentDeletable(@Param("p_content_id") int contentId);
}

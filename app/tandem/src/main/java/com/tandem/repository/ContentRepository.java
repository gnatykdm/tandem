package com.tandem.repository;

import com.tandem.model.entity.ContentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface ContentRepository extends JpaRepository<ContentEntity, Long> {
    @Query(value = "SELECT add_content(:p_photo, :p_video, :p_audio)", nativeQuery = true)
    Long addContent(
            @Param("p_photo") Long photoId,
            @Param("p_video") Long videoId,
            @Param("p_audio") Long audioId
    );
}

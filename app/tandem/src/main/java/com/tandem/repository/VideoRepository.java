package com.tandem.repository;

import com.tandem.model.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

    @Query(value = "SELECT * FROM Video v " +
            "JOIN \"Content\" c ON v.video_id = c.video " +
            "JOIN Optional o ON c.content_id = o.group_id " +
            "JOIN \"User\" u ON o.user_id = u.id " +
            "WHERE u.id = :userId", nativeQuery = true)
    List<VideoEntity> getAllVideosByUser(@Param("userId") Long userId);
}

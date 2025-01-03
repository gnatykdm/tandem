package com.tandem.repository;

import com.tandem.model.entity.AudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioRepository extends JpaRepository<AudioEntity, Long> {
    @Query(value = "SELECT a.* FROM audio a " +
            "JOIN \"Content\" c ON a.audio_id = c.audio " +
            "JOIN Optional o ON c.content_id = o.group_id " +
            "JOIN \"User\" u ON o.user_id = u.id " +
            "WHERE u.id = :userId", nativeQuery = true)
    List<AudioEntity> getAllAudioByUser(@Param("userId") Long userId);
}

package com.tandem.repository;

import com.tandem.model.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    @Query(value = "SELECT * FROM Photo p " +
            "JOIN \"Content\" c ON p.photo_id = c.photo " +
            "JOIN Optional o ON c.content_id = o.group_id " +
            "JOIN \"User\" u ON o.user_id = u.id " +
            "WHERE u.id = :userId", nativeQuery = true)
    List<PhotoEntity> getAllPhotosByUser(@Param("userId") Long userId);
}

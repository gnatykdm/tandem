package com.tandem.repository;

import com.tandem.model.entity.TextEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<TextEntity, Long> {

    @Query(value = "SELECT * FROM Text t " +
            "JOIN \"Content\" c ON t.text_id = c.text " +
            "JOIN Optional o ON c.content_id = o.group_id " +
            "JOIN \"User\" u ON o.user_id = u.id " +
            "WHERE u.id = :userId", nativeQuery = true)
    List<TextEntity> getAllTextByUser(@Param("userId") Long userId);
}

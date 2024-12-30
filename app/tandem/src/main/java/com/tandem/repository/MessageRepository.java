package com.tandem.repository;

import com.tandem.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findBySenderId(Long senderId);
    List<MessageEntity> findByGroupId(Long groupId);
    List<MessageEntity> findByContentContaining(String keyword);
}
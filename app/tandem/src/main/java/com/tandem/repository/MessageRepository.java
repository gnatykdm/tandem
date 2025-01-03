package com.tandem.repository;

import com.tandem.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "CALL message_management.add_message(:sender, :content, :sendAt)", nativeQuery = true)
    void addMessage(@Param("sender") Long sender, @Param("content") String content, @Param("sendAt") LocalDateTime sendAt);

    @Transactional
    @Modifying
    @Query(value = "CALL message_management.update_message(:messageId, :content, :sendAt)", nativeQuery = true)
    void updateMessage(@Param("messageId") Long messageId, @Param("content") String content, @Param("sendAt") LocalDateTime sendAt);

    @Query(value = "SELECT * FROM message_management.get_message_by_id(:messageId)", nativeQuery = true)
    List<Object[]> getMessageById(@Param("messageId") Long messageId);

    @Transactional
    @Modifying
    @Query(value = "CALL message_management.delete_message(:messageId)", nativeQuery = true)
    void deleteMessage(@Param("messageId") Long messageId);

    @Query(value = "SELECT * FROM message_management.get_messages_by_user(:userId)", nativeQuery = true)
    List<MessageEntity> getAllMessageSentByUser(@Param("userId") Long userId);
}


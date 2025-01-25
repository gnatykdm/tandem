package com.tandem.repository;

import com.tandem.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {


    @Query(value = "SELECT * FROM group_management.get_group_by_name(:groupName)", nativeQuery = true)
    GroupEntity getGroupByName(@Param("groupName") String groupName);

    @Procedure(procedureName = "group_management.add_user_to_group")
    void addUserToGroup(
            @Param("p_user_id") Long userId,
            @Param("p_group_id") Long groupId,
            @Param("p_admin") boolean isAdmin
    );

    @Query(value = "SELECT * FROM group_management.list_group_users(:groupId)", nativeQuery = true)
    List<Object[]> listGroupUsers(@Param("groupId") Long groupId);

    @Modifying
    @Transactional
    @Query(value = "CALL group_management.remove_user_from_group(:p_user_id, :p_group_id)", nativeQuery = true)
    void removeUserFromGroup(
            @Param("p_user_id") Long userId,
            @Param("p_group_id") Long groupId
    );

    @Query(value = "SELECT group_management.check_admin_rights(:userId, :groupId)", nativeQuery = true)
    Boolean checkAdminRights(
            @Param("userId") Long userId,
            @Param("groupId") Long groupId
    );

    @Procedure(procedureName = "content_management.delete_content")
    void deleteContent(@Param("p_content_id") Long contentId);

    @Query(value = "SELECT * FROM content_management.get_content_by_group(:groupId)", nativeQuery = true)
    List<Object[]> getContentByGroup(@Param("groupId") Long groupId);

    @Modifying
    @Transactional
    @Procedure(procedureName = "message_management.add_message")
    void addMessage(
            @Param("p_sender") Long senderId,
            @Param("p_content") String content,
            @Param("p_group_id") Long groupId,
            @Param("p_send_at") LocalDateTime sendAt
    );

    @Query(value = "SELECT * FROM group_management.get_messages_by_group(:p_group_id)", nativeQuery = true)
    List<Object[]> getMessagesByGroup(@Param("p_group_id") Long groupId);

    @Query("SELECT m FROM MessageEntity m WHERE m.senderMessage.id = :userId")
    List<MessageEntity> getMessagesByUser(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM group_management.\"Group\"", nativeQuery = true)
    List<GroupEntity> getAllGroups();

    @Query(value = "SELECT * FROM content_management.get_content_by_group(:groupId) WHERE content_type = 'video'", nativeQuery = true)
    List<VideoEntity> getAllVideosFromGroup(@Param("groupId") Long groupId);

    @Query(value = "SELECT * FROM content_management.get_content_by_group(:groupId) WHERE content_type = 'photo'", nativeQuery = true)
    List<PhotoEntity> getAllPhotosFromGroup(@Param("groupId") Long groupId);

    @Query(value = "SELECT * FROM content_management.get_content_by_group(:groupId) WHERE content_type = 'audio'", nativeQuery = true)
    List<AudioEntity> getAllAudioFromGroup(@Param("groupId") Long groupId);

    @Procedure(procedureName = "group_management.create_group")
    void createGroup(
            @Param("p_group_name") String groupName,
            @Param("p_group_icon") String groupIcon,
            @Param("p_group_description") String groupDescription,
            @Param("p_access_code") String accessCode,
            @Param("p_type") Boolean type
    );

    @Procedure(procedureName = "group_management.delete_group")
    void deleteGroup(@Param("p_group_id") Long groupId);

    @Modifying
    @Transactional
    @Query(value = "CALL group_management.update_group(:groupId, :groupIcon, :groupDescription)", nativeQuery = true)
    void updateGroup(@Param("groupId") Long groupId,
                     @Param("groupIcon") String groupIcon,
                     @Param("groupDescription") String groupDescription);


}

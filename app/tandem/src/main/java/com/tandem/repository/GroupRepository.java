package com.tandem.repository;

import com.tandem.model.entity.GroupEntity;
import com.tandem.model.entity.VideoEntity;
import com.tandem.model.entity.PhotoEntity;
import com.tandem.model.entity.MessageEntity;
import com.tandem.model.entity.AudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    @Procedure(name = "group_management.add_user_to_group")
    void addUserToGroup(@Param("p_user_id") Long userId, @Param("p_group_id") Long groupId, @Param("p_admin") boolean admin);

    @Procedure(name = "group_management.remove_user_from_group")
    void removeUserFromGroup(@Param("p_user_id") Long userId, @Param("p_group_id") Long groupId);

    @Query(value = "SELECT * FROM group_management.list_group_users(:group_id)", nativeQuery = true)
    List<GroupEntity> listGroupUsers(@Param("group_id") Long groupId);

    @Query(value = "SELECT group_management.check_admin_rights(:user_id, :group_id)", nativeQuery = true)
    boolean checkAdminRights(@Param("user_id") Long userId, @Param("group_id") Long groupId);

    @Query(value = "SELECT * FROM group_management.list_groups()", nativeQuery = true)
    List<GroupEntity> listGroups();

    @Query(value = "SELECT v.* FROM Video v " +
            "JOIN \"Content\" c ON v.video_id = c.video " +
            "JOIN \"Group\" g ON c.content_id = g.group_id " +
            "WHERE g.group_id = :groupId", nativeQuery = true)
    List<VideoEntity> getAllVideosFromGroup(@Param("groupId") Long groupId);

    @Query(value = "SELECT p.* FROM Photo p " +
            "JOIN \"Content\" c ON p.photo_id = c.photo " +
            "JOIN \"Group\" g ON c.content_id = g.group_id " +
            "WHERE g.group_id = :groupId", nativeQuery = true)
    List<PhotoEntity> getAllPhotosFromGroup(@Param("groupId") Long groupId);

    @Query(value = "SELECT m.* FROM \"Message\" m " +
            "JOIN \"Group\" g ON m.message_id = g.messages " +
            "WHERE g.group_id = :groupId", nativeQuery = true)
    List<MessageEntity> getAllMessagesFromGroup(@Param("groupId") Long groupId);

    @Query(value = "SELECT a.* FROM Audio a " +
            "JOIN \"Content\" c ON a.audio_id = c.audio " +
            "JOIN \"Group\" g ON c.content_id = g.group_id " +
            "WHERE g.group_id = :groupId", nativeQuery = true)
    List<AudioEntity> getAllAudioFromGroup(@Param("groupId") Long groupId);
}

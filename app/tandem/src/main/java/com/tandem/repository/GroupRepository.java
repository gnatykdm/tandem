package com.tandem.repository;

import com.tandem.model.entity.GroupEntity;
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
    List<GroupEntity> listGroupUsers(@Param("group_id") int groupId);

    @Query(value = "SELECT group_management.check_admin_rights(:user_id, :group_id)", nativeQuery = true)
    boolean checkAdminRights(@Param("user_id") int userId, @Param("group_id") int groupId);

    @Query(value = "SELECT * FROM group_management.list_groups()", nativeQuery = true)
    List<GroupEntity> listGroups();
}



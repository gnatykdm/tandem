package com.tandem.repository;

import com.tandem.model.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    @Procedure(procedureName = "add_user_to_group")
    void addUserToGroup(
            @Param("p_user_id") Long userId,
            @Param("p_group_id") Long groupId,
            @Param("p_admin") Boolean admin
    );

    @Query(value = "SELECT * FROM list_group_users(:p_group_id)", nativeQuery = true)
    List<Object[]> listGroupUsers(@Param("p_group_id") Long groupId);

    @Procedure(procedureName = "remove_user_from_group")
    void removeUserFromGroup(
            @Param("p_user_id") Long userId,
            @Param("p_group_id") Long groupId
    );

    @Procedure(procedureName = "invite_to_group")
    void inviteToGroup(
            @Param("p_user_id") Long userId,
            @Param("p_group_id") Long groupId,
            @Param("p_access_code") String accessCode
    );
}

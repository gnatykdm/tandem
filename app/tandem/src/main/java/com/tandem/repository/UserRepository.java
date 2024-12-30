package com.tandem.repository;

import com.tandem.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Procedure(procedureName = "add_user")
    void addUser(
            @Param("p_login") String login,
            @Param("p_username") String username,
            @Param("p_email") String email,
            @Param("p_password") String password,
            @Param("p_key") String key,
            @Param("p_about") String about,
            @Param("p_profile_image") String profileImage
    );

    @Procedure(procedureName = "update_user_profile")
    void updateUserProfile(
            @Param("p_id") Long id,
            @Param("p_about") String about,
            @Param("p_profile_image") String profileImage
    );
}

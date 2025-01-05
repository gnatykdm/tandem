package com.tandem.repository;

import com.tandem.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM user_management.get_user_by_login(:login)", nativeQuery = true)
    Optional<UserEntity> getUserByLogin(@Param("login") String login);

    @Query(value = "SELECT * FROM user_management.get_user_by_email(:email)", nativeQuery = true)
    Optional<UserEntity> getUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM user_management.get_all_users()", nativeQuery = true)
    List<UserEntity> getAllUsers();

    @Procedure(value = "user_management.add_user")
    void addUser(@Param("p_login") String login,
                 @Param("p_username") String username,
                 @Param("p_email") String email,
                 @Param("p_password") String password,
                 @Param("p_about") String about,
                 @Param("p_profile_image") String profileImage);

    @Query(value = "SELECT user_management.check_authorization(:login, :password)", nativeQuery = true)
    boolean checkAuthorization(@Param("login") String login, @Param("password") String password);

    @Procedure(value = "user_management.follow_user")
    void followUser(@Param("p_follower_id") Long followerId, @Param("p_following_id") Long followingId);

    @Procedure(value = "user_management.unfollow_user")
    void unfollowUser(@Param("p_follower_id") Long followerId, @Param("p_following_id") Long followingId);

    @Procedure("user_management.delete_user")
    void deleteUserById(@Param("p_id") Long id);

    @Modifying
    @Procedure(value = "user_management.update_user_profile")
    void updateUserProfile(@Param("p_id") Long id,
                           @Param("p_about") String about,
                           @Param("p_profile_image") String profileImage,
                           @Param("p_username") String username);

}

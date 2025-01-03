package com.tandem.repository;

import com.tandem.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);

    @Procedure(name = "user_management.add_user")
    void addUser(String pLogin, String pUsername, String pEmail, String pPassword, String pKey, String pAbout, String pProfileImage);

    @Query(value = "SELECT user_management.check_authorization(:login, :password)", nativeQuery = true)
    boolean checkAuthorization(@Param("login") String login, @Param("password") String password);

    @Query(value = "SELECT * FROM user_management.get_user_by_id(:id)", nativeQuery = true)
    UserEntity getUserById(@Param("id") Long id);

    @Procedure(name = "user_management.follow_user")
    void followUser(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Procedure(name = "user_management.unfollow_user")
    void unfollowUser(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    @Query(value = "SELECT COUNT(*) FROM follows WHERE followers = :followerId AND following = :followingId", nativeQuery = true)
    int isUserFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
}

package com.tandem.service.user;

import com.tandem.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserService {
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);

    void addUser(String pLogin, String pUsername,
                 String pEmail, String pPassword,
                 String pKey, String pAbout, String pProfileImage);

    boolean checkAuthorization(String login, String password);
    UserEntity getUserById(Long id);
    List<UserEntity> getAllUsers();
    void followUser(Long followerId, Long followingId);
    void unfollowUser(Long followerId, Long followingId);
    int isUserFollowing(Long followerId, Long followingId);
}

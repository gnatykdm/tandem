package com.tandem.service.user;

import com.tandem.model.dto.UpdateUserDTO;
import com.tandem.model.dto.UserDTO;
import com.tandem.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserService {
    void addUser(UserDTO user);
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);
    UserEntity getUserById(Long userId);
    boolean checkAuthorization(String login, String password);
    void deleteUserByLogin(String login);
    void updateUserByLogin(UpdateUserDTO updateUserDTO, String login);
    List<UserEntity> getAllUsers();
    void followUser(Long followerId, Long followingId);
    void unfollowUser(Long followerId, Long followingId);
    boolean isUserFollow(Long followerId, Long followingId);
    List<UserEntity> getFollowers(Long userId);
    List<UserEntity> getFollowing(Long userId);
}

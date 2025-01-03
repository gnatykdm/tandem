package com.tandem.service.user;

import com.tandem.model.entity.UserEntity;
import com.tandem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        logger.info("UserService - findByLogin: Searching for user with login: {}", login);
        return userRepository.findByLogin(login);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        logger.info("UserService - findByEmail: Searching for user with email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public void addUser(String pLogin, String pUsername,
                        String pEmail, String pPassword,
                        String pKey, String pAbout, String pProfileImage) {
        logger.info("UserService - addUser: Adding a new user");
        try {
            userRepository.addUser(pLogin, pUsername, pEmail, pPassword, pKey, pAbout, pProfileImage);
        } catch (Exception e) {
            logger.error("UserService - addUser: Error while adding user", e);
            throw new RuntimeException("Failed to add user", e);
        }
    }

    @Override
    public boolean checkAuthorization(String login, String password) {
        logger.info("UserService - checkAuthorization: Checking authorization for login: {}", login);
        return userRepository.checkAuthorization(login, password);
    }

    @Override
    public UserEntity getUserById(Long id) {
        logger.info("UserService - getUserById: Retrieving user with ID: {}", id);
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        logger.info("UserService - getAllUsers: Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public void followUser(Long followerId, Long followingId) {
        if (followerId < 0 || followingId < 0) {
            logger.error("UserService - followUser: Follower od or following id can't be negative");
            throw new IllegalArgumentException("UserService - followUser: Follower od or following id can't be negative");
        }

        logger.info("UserService - followUser: User with id: {} start follow user: {}", followerId, followingId);
        userRepository.followUser(followerId, followingId);
    }

    @Override
    public void unfollowUser(Long followerId, Long followingId) {
        if (followerId < 0 || followingId < 0) {
            logger.error("UserService - unfollowUser: Follower od or following id can't be negative");
            throw new IllegalArgumentException("UserService - unfollowUser: Follower od or following id can't be negative");
        }

        logger.info("UserService - unfollowUser: User with id: {} unfollow user: {}", followerId, followingId);
        userRepository.unfollowUser(followerId, followingId);
    }

    @Override
    public int isUserFollowing(Long followerId, Long followingId) {
        if (followerId < 0 || followingId < 0) {
            logger.error("UserService - isUserFollowing: Follower od or following id can't be negative");
            throw new IllegalArgumentException("UserService - isUserFollowing: Follower od or following id can't be negative");
        }

        logger.info("UserService - isUserFollowing: User: {} following: {}", followerId, followingId);
        return userRepository.isUserFollowing(followerId, followingId);
    }
}

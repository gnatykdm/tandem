package com.tandem.service.user;

import com.tandem.model.dto.UpdateUserDTO;
import com.tandem.model.dto.UserDTO;
import com.tandem.model.entity.UserEntity;
import com.tandem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return userRepository.getUserByLogin(login);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        logger.info("UserService - findByEmail: Searching for user with email: {}", email);
        return userRepository.getUserByEmail(email);
    }

    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }


    @Transactional
    @Override
    public void addUser(UserDTO userDTO) {
        logger.info("UserService - addUser: Adding a new user");

        userRepository.addUser(
                userDTO.getLogin(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getAbout(),
                userDTO.getProfileImage()
        );
    }

    @Override
    public boolean checkAuthorization(String login, String password) {
        logger.info("UserService - checkAuthorization: Checking authorization for login: {}", login);
        return userRepository.checkAuthorization(login, password);
    }

    @Override
    @Transactional
    public void deleteUserByLogin(String login) {
        if (login.isEmpty()) {
            logger.error("UserService - deleteUserByLogin: Login can't be empty");
        }

        Optional<UserEntity> user = userRepository.getUserByLogin(login);
        if (user.isPresent()) {
            logger.info("UserService - deleteUserByLogin: Deleting user: {}", login);
            userRepository.deleteUserById(user.get().getId());
        } else {
            logger.error("Not find user with login: {}", login);
        }
    }

    @Override
    @Transactional
    public void updateUserByLogin(UpdateUserDTO updateUserDTO, String login) {
        if (login == null || login.isEmpty()) {
            logger.error("UserService - updateUserByLogin: Login can't be null or empty");
            return;
        }

        Optional<UserEntity> user = userRepository.getUserByLogin(login);
        if (user.isPresent()) {
            logger.info("UserService - updateUserByLogin: Updating user: {}", login);

            userRepository.updateUserProfile(user.get().getId(),
                    updateUserDTO.getAbout(),
                    updateUserDTO.getImage(),
                    updateUserDTO.getUsername());
        } else {
            logger.error("UserService - updateUserByLogin: User with login {} not found", login);
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        logger.info("UserService - getAllUsers: Fetching all users");
        return userRepository.getAllUsers();
    }

    @Override
    public void followUser(Long followerId, Long followingId) {
        if (followerId < 0 || followingId < 0) {
            logger.error("UserService - followUser: User id's can't be negative");
        }

        logger.info("UserService - followUser: User: " + followerId + " start following: " + followingId);
        userRepository.followUser(followerId, followingId);
    }

    @Override
    public void unfollowUser(Long followerId, Long followingId) {
        if (followerId < 0 || followingId < 0) {
            logger.error("UserService - unfollowUser: User id's can't be negative");
        }

        logger.info("UserService - unfollowUser: User: " + followerId + " stop following: " + followingId);
        userRepository.unfollowUser(followerId, followingId);
    }

    @Override
    public boolean isUserFollow(Long followerId, Long followingId) {
        if (followerId < 0 || followingId < 0) {
            logger.error("UserService - isUserFollow: User id's can't be negative");
        }

        return userRepository.isUserFollowing(followerId, followingId);
    }

    @Override
    public List<UserEntity> getFollowers(Long userId) {
        if (userId < 0) {
            logger.error("UserService - getFollowers: User id can't be negative");
        }

        List<UserEntity> followers = userRepository.getFollowers(userId);
        if (!followers.isEmpty()) {
            return followers;
        } else {
            return null;
        }
    }

    @Override
    public List<UserEntity> getFollowing(Long userId) {
        if (userId < 0) {
            logger.error("UserService - getFollowing: User id can't be negative");
        }

        List<UserEntity> followings = userRepository.getFollowing(userId);
        if (!followings.isEmpty()) {
            return followings;
        } else {
            return null;
        }
    }
}
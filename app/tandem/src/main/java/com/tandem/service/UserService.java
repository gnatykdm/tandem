package com.tandem.service;

import com.tandem.model.entity.UserEntity;
import com.tandem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public void addUser(String login, String username,
                        String email, String password,
                        String key, String about,
                        String profileImage) {
        try {
            UserEntity user = new UserEntity(login, username, email, password, key, about, profileImage);
            userRepository.save(user);
            logger.info("User created successfully: {}", username);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            throw new RuntimeException("Error creating user", e);
        }
    }

    public void updateUserProfile(Long id, String about, String profileImage) {
        try {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setAbout(about);
            user.setProfileImage(profileImage);

            userRepository.save(user);
            logger.info("User profile updated successfully: {}", user.getUsername());
        } catch (RuntimeException e) {
            logger.error("Error updating user profile: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            throw new RuntimeException("Error updating user profile", e);
        }
    }

    public UserEntity getUserById(Long id) {
        if (id == null || id < 0) {
            logger.error("Error getting user by id: invalid id {}", id);
            throw new IllegalArgumentException("Invalid user id: " + id);
        }

        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            logger.error("User not found with id: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public void deleteUserById(Long id) {
        if (id == null || id < 0) {
            logger.error("Error deleting user by id: invalid id {}", id);
            throw new IllegalArgumentException("Invalid user id: " + id);
        }

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("User deleted successfully: {}", id);
        } else {
            logger.error("User not found with id: {}", id);
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public List<UserEntity> getAll() {
        logger.info("Getting All Users");
        return userRepository.findAll();
    }
}

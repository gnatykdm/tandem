package com.tandem.controller;

import com.tandem.model.dto.UpdateUserDTO;
import com.tandem.model.dto.UserDTO;
import com.tandem.model.entity.AudioEntity;
import com.tandem.model.entity.PhotoEntity;
import com.tandem.model.entity.UserEntity;
import com.tandem.model.entity.VideoEntity;
import com.tandem.service.content.IContentService;
import com.tandem.service.s3.IS3Connection;
import com.tandem.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IContentService contentService;

    @Autowired
    private IS3Connection s3Connection;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/find_by_login/{login}")
    public ResponseEntity<?> getUserByLogin(@PathVariable String login) {
        if (login.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserController - getUserByLogin: Login can't be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        if (user.isPresent()) {
            return ResponseEntity.ok(wrapUserEntity(user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with login: " + login + " not found.");
        }
    }

    @GetMapping("/find_by_email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        if (email.isEmpty() || !isEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserController - getUserByEmail: Invalid email format");
        }

        Optional<UserEntity> user = userService.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(wrapUserEntity(user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email: " + email + " not found.");
        }
    }

    @GetMapping("/find_by_id/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User id can't be null");
        }

        UserDTO userDTO = wrapUserEntity(userService.getUserById(userId));
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/find_all")
    public ResponseEntity<?> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found.");
        }

        List<UserDTO> userDTOs = users.stream()
                .map(this::wrapUserEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @DeleteMapping("/delete/{login}")
    public ResponseEntity<String> deleteUser(@PathVariable String login) {
        s3Connection.deleteUser(login);
        userService.deleteUserByLogin(login);
        return ResponseEntity.status(HttpStatus.OK).body("User with login: " + login + " has been deleted");
    }

    @PutMapping("/update/{login}")
    public ResponseEntity<String> updateUser(@PathVariable String login, @RequestBody UpdateUserDTO updateUserDTO) {
        if (login == null || login.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login cannot be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with login " + login + " not found");
        }

        if (updateUserDTO.getAbout() == null || updateUserDTO.getAbout().isEmpty()) {
            updateUserDTO.setAbout(user.get().getAbout());
        }
        if (updateUserDTO.getUsername() == null || updateUserDTO.getUsername().isEmpty()) {
            updateUserDTO.setUsername(user.get().getUsername());
        }
        if (updateUserDTO.getImage() == null || updateUserDTO.getImage().isEmpty()) {
            updateUserDTO.setImage(user.get().getProfileImage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with login: " + login + " not found");
        }

        try {
            userService.updateUserByLogin(updateUserDTO, login);
            return ResponseEntity.status(HttpStatus.OK).body("User data for login: " + login + " has been updated");
        } catch (Exception e) {
            logger.error("Error occurred while updating user with login {}: {}", login, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the user.");
        }
    }


    @GetMapping("/get_post_count/{login}")
    public ResponseEntity<?> getAllUserPostsCount(@PathVariable String login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login can't be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        if (user.isPresent()) {
            List<VideoEntity> video = contentService.getVideosByUser(user.get().getId());
            List<PhotoEntity> photo = contentService.getPhotosByUser(user.get().getId());
            List<AudioEntity> audio = contentService.getAudiosByUser(user.get().getId());

            return ResponseEntity.ok(video.size() + photo.size() + audio.size());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with login: " + login + " not found");
        }
    }

    private UserDTO wrapUserEntity(UserEntity user) {
        return new UserDTO(
                user.getLogin(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getAbout(),
                user.getProfileImage()
        );
    }

    private boolean isEmail(String s) {
        return s.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }
}

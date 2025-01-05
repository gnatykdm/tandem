package com.tandem.controller;

import com.tandem.model.dto.UpdateUserDTO;
import com.tandem.model.dto.UserDTO;
import com.tandem.model.entity.UserEntity;
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
        userService.deleteUserByLogin(login);
        return ResponseEntity.status(HttpStatus.OK).body("User with login: " + login + " has been deleted");
    }

    @PutMapping("/update/{login}")
    public ResponseEntity<String> updateUser(@PathVariable String login, @RequestBody UpdateUserDTO updateUserDTO) {
        if (login == null || login.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login cannot be empty");
        }

        try {
            userService.updateUserByLogin(updateUserDTO, login);
            return ResponseEntity.status(HttpStatus.OK).body("User data for login: " + login + " has been updated");
        } catch (Exception e) {
            logger.error("Error occurred while updating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the user.");
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

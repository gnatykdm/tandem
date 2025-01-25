package com.tandem.controller;

import com.tandem.model.dto.UserDTO;
import com.tandem.model.entity.UserEntity;
import com.tandem.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/follows")
public class FollowController {

    @Autowired
    private IUserService userService;

    @PostMapping("/follow/{login}")
    public ResponseEntity<String> followUser(@PathVariable String login,
                                             @RequestParam String follow) {
        if (login.isEmpty() || follow.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login can't be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        Optional<UserEntity> followingUser = userService.findByLogin(follow);

        if (user.isPresent() && followingUser.isPresent()) {
            userService.followUser(user.get().getId(), followingUser.get().getId());
            return ResponseEntity.ok("User: " + login + " start follow: " + follow);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cant find users");
        }
    }

    @DeleteMapping("/unfollow/{login}")
    public ResponseEntity<String> unFollowUser(@PathVariable String login,
                                             @RequestParam String follow) {
        if (login.isEmpty() || follow.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login can't be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        Optional<UserEntity> followingUser = userService.findByLogin(follow);

        if (user.isPresent() && followingUser.isPresent()) {
            userService.unfollowUser(user.get().getId(), followingUser.get().getId());
            return ResponseEntity.ok("User: " + login + " stop follow: " + follow);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cant find users");
        }
    }

    @GetMapping("/is_user_follow/{login}")
    public ResponseEntity<?> isUserFollow(@PathVariable String login,
                                          @RequestParam String follow) {
        if (login.isEmpty() || follow.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Users name can't be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        Optional<UserEntity> followingUser = userService.findByLogin(follow);

        if (user.isPresent() && followingUser.isPresent()) {
            boolean status = userService.isUserFollow(user.get().getId(), followingUser.get().getId());
            return ResponseEntity.ok(status);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cant find users");
        }
    }

    @GetMapping("/get_followers/{login}")
    public ResponseEntity<?> getFollowers(@PathVariable String login) {
        if (login == null || login.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login cannot be empty.");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with login: " + login + " not found.");
        }

        List<UserDTO> followers = userService.getFollowers(user.get().getId()).stream()
                .map(this::wrapUserEntity)
                .collect(Collectors.toList());
        if (followers.isEmpty()) {
            return ResponseEntity.ok("No followers found for user: " + login);
        } else {
            return ResponseEntity.ok(followers);
        }
    }

    @GetMapping("/get_following/{login}")
    public ResponseEntity<?> getFollowing(@PathVariable String login) {
        if (login == null || login.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login cannot be empty.");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with login: " + login + " not found.");
        }

        List<UserDTO> followers = userService.getFollowing(user.get().getId()).stream()
                .map(this::wrapUserEntity)
                .collect(Collectors.toList());
        if (followers.isEmpty()) {
            return ResponseEntity.ok("No following found for user: " + login);
        } else {
            return ResponseEntity.ok(followers);
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
}

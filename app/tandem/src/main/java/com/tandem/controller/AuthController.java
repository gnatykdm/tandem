package com.tandem.controller;

import com.tandem.model.dto.UserDTO;
import com.tandem.model.entity.UserEntity;
import com.tandem.service.file.FileService;
import com.tandem.service.mail.IMailService;
import com.tandem.service.s3.IS3Connection;
import com.tandem.service.user.IUserService;
import com.tandem.utils.GenKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IS3Connection is3Connection;

    @Autowired
    private IMailService mailService;

    @Autowired
    private FileService defaultLogo;

    private int verifyKey;
    private UserDTO user;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestParam("login") String login,
                                               @RequestParam("username") String username,
                                               @RequestParam("email") String email,
                                               @RequestParam("password") String password) {

        Optional<UserEntity> check = userService.findByLogin(login);
        if (check.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with login: " + login + " already exists");
        }

        GenKey genKey = new GenKey();
        Integer key = genKey.genKey();
        this.verifyKey = key;
        mailService.sendMail(email, String.valueOf(this.verifyKey));

        this.user = new UserDTO(login, username, email, password, null, null);
        return ResponseEntity.ok("Verification email sent. Please check your inbox.");
    }

    @PutMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String log,
                                            @RequestParam String pass) {

        Optional<UserEntity> userOpt = userService.findByLogin(log);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid login or password");
        }

        UserEntity user = userOpt.get();
        boolean status = userService.checkAuthorization(log, pass);

        if (status) {
            return ResponseEntity.ok("User logged in successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong password");
        }
    }

    @PutMapping("/verify")
    public ResponseEntity<String> checkAuth(@RequestParam String log, @RequestParam String pass) {
        if (log.isEmpty() || pass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login or password can't be null");
        }

        boolean status = userService.checkAuthorization(log, pass);
        if (status) {
            return ResponseEntity.ok("User login successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid login or password");
        }
    }

    @PutMapping("/validate_key")
    public ResponseEntity<?> checkVerifyKey(@RequestParam String k) throws IOException {
        if (k.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verify key can't be empty");
        }

        int key;
        try {
            key = Integer.parseInt(k);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid key format");
        }

        if (key == this.verifyKey) {
            is3Connection.createUserFolder(this.user.getLogin());
            String iconUrl = is3Connection.uploadUserIcon(defaultLogo.getPngFileAsFile(), user.getLogin());
            this.user.setProfileImage(iconUrl);

            userService.addUser(this.user);
            return ResponseEntity.ok().body("User created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification key");
        }
    }
}

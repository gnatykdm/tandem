package com.tandem.controller;

import com.tandem.model.dto.UserDTO;
import com.tandem.service.s3.IS3Connection;
import com.tandem.service.user.IUserService;
import com.tandem.utils.GenKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IS3Connection is3Connection;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private int verifyKey;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        if (userDTO == null) {
            logger.error("UserController - registerUser: UserDTO can't be null");
            return ResponseEntity.badRequest().body("UserDTO can't be null");
        }

        try {
            userService.addUser(userDTO);
            is3Connection.createUserFolder(userDTO.getLogin());
            return ResponseEntity.ok("User registered successfully.");
        } catch (Exception e) {
            logger.error("UserController - registerUser: Error while registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
        }
    }

    @PutMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String log,
                                            @RequestParam String pass) {
        boolean status = userService.checkAuthorization(log, pass);
        if (status) {
            return ResponseEntity.ok("User login successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong password");
        }
    }

    @PutMapping("/verify")
    public ResponseEntity<String> checkAuth(@RequestParam String log, @RequestParam String pass) {
        if (log.isEmpty() || pass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login or user can't be null");
        }

        boolean status = userService.checkAuthorization(log, pass);
        if (status) {
            return ResponseEntity.ok("User login successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid login or password");
        }
    }

    @GetMapping("/generate_key")
    public ResponseEntity<String> getVerifyKey() {
        GenKey genKey = new GenKey();
        this.verifyKey = genKey.genKey();
        return ResponseEntity.ok(String.valueOf(this.verifyKey));
    }

    @PutMapping("/validate_key")
    public ResponseEntity<String> checkVerifyKey(@RequestParam String k) {
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
            return ResponseEntity.ok("Verification succeed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification key");
        }
    }

}

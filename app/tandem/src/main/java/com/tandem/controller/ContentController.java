package com.tandem.controller;

import com.tandem.model.entity.UserEntity;
import com.tandem.service.content.IContentService;
import com.tandem.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    @Autowired
    private IContentService contentService;

    @Autowired
    private IUserService userService;

    @GetMapping("/get_all_photos_by_id/{log}")
    public ResponseEntity<?> getAllPhotosByUserLogin(@PathVariable String log) {
        if (log == null || log.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login cannot be empty or blank.");
        }

        Optional<UserEntity> user = userService.findByLogin(log);
        if (user.isPresent()) {
            List<Object[]> photos = contentService.getPhotosByUser(user.get().getId());
            if (!photos.isEmpty()) {
                return ResponseEntity.ok(photos);
            } else {
                return ResponseEntity.ok("User: " + log + " not have photos");
            }
        } else {
            return ResponseEntity.ok("User: " + log + " not exist");
        }
    }

    @GetMapping("/get_all_videos_by_id/{log}")
    public ResponseEntity<?> getAllVideosByUserLogin(@PathVariable String log) {
        if (log == null || log.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login cannot be empty or blank.");
        }

        Optional<UserEntity> user = userService.findByLogin(log);
        if (user.isPresent()) {
            List<Object[]> photos = contentService.getVideosByUser(user.get().getId());
            if (!photos.isEmpty()) {
                return ResponseEntity.ok(photos);
            } else {
                return ResponseEntity.ok("User: " + log + " not have photos");
            }
        } else {
            return ResponseEntity.ok("User: " + log + " not exist");
        }
    }

    @GetMapping("/get_all_audio_by_id/{log}")
    public ResponseEntity<?> getAllAudiosByUserLogin(@PathVariable String log) {
        if (log == null || log.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login cannot be empty or blank.");
        }

        Optional<UserEntity> user = userService.findByLogin(log);
        if (user.isPresent()) {
            List<Object[]> photos = contentService.getAudiosByUser(user.get().getId());
            if (!photos.isEmpty()) {
                return ResponseEntity.ok(photos);
            } else {
                return ResponseEntity.ok("User: " + log + " not have photos");
            }
        } else {
            return ResponseEntity.ok("User: " + log + " not exist");
        }
    }

    @GetMapping("/get_all_text_by_id/{log}")
    public ResponseEntity<?> getAllTextByUserLogin(@PathVariable String log) {
        if (log == null || log.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login cannot be empty or blank.");
        }

        Optional<UserEntity> user = userService.findByLogin(log);
        if (user.isPresent()) {
            List<Object[]> photos = contentService.getTextsByUser(user.get().getId());
            if (!photos.isEmpty()) {
                return ResponseEntity.ok(photos);
            } else {
                return ResponseEntity.ok("User: " + log + " not have photos");
            }
        } else {
            return ResponseEntity.ok("User: " + log + " not exist");
        }
    }
}

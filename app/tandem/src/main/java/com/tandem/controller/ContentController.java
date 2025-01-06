package com.tandem.controller;

import com.tandem.model.entity.PhotoEntity;
import com.tandem.model.entity.UserEntity;
import com.tandem.repository.ContentRepository;
import com.tandem.service.content.IContentService;
import com.tandem.service.s3.IS3Connection;
import com.tandem.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    @Autowired
    private IContentService contentService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private IS3Connection s3Connection;

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

    @DeleteMapping("/delete_photo/{id}")
    public ResponseEntity<String> deletePhotoById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content id can't be negative");
        }

        contentRepository.deletePhoto(id);
        return ResponseEntity.ok("Photo with id: " + id + " was deleted");
    }

    @DeleteMapping("/delete_video/{id}")
    public ResponseEntity<String> deleteVideoById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content id can't be negative");
        }

        contentRepository.deleteVideo(id);
        return ResponseEntity.ok("Video with id: " + id + " was deleted");
    }

    @DeleteMapping("/delete_text/{id}")
    public ResponseEntity<String> deleteTextById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content id can't be negative");
        }

        contentRepository.deleteTextContent(id);
        return ResponseEntity.ok("Text with id: " + id + " was deleted");
    }

    @DeleteMapping("/delete_audio/{id}")
    public ResponseEntity<String> deleteAudioById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content id can't be negative");
        }

        contentRepository.deleteAudio(id);
        return ResponseEntity.ok("Audio with id: " + id + " was deleted");
    }

    @PostMapping("/add_photo/{login}")
    public ResponseEntity<String> createPhotoByLogin(@RequestParam String description,
                                                     @RequestParam("file") MultipartFile file, @PathVariable String login) {
        if (login.isEmpty() || file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login or file can't be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        if (user.isPresent()) {
            String photoUrl = s3Connection.uploadPhoto(file, login);
            contentService.createPhoto(photoUrl, description, user.get().getId());
            return ResponseEntity.ok("Photo was uploaded to user: " + login);
        } else {
            return ResponseEntity.ok("User with login: " + login + " not found");
        }
    }

    @PostMapping("/add_video/{login}")
    public ResponseEntity<String> createVideoByLogin(@RequestParam String description,
                                                     @RequestParam("file") MultipartFile file, @PathVariable String login) {
        if (login.isEmpty() || file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login or file can't be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        if (user.isPresent()) {
            String photoUrl = s3Connection.uploadVideo(file, login);
            contentService.createVideo(photoUrl, description, user.get().getId());
            return ResponseEntity.ok("Video was uploaded to user: " + login);
        } else {
            return ResponseEntity.ok("User with login: " + login + " not found");
        }
    }

    @PostMapping("/add_audio/{login}")
    public ResponseEntity<String> createAudioByLogin( @RequestParam("file") MultipartFile file,
                                                      @PathVariable String login) {
        if (login.isEmpty() || file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login or file can't be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        if (user.isPresent()) {
            String photoUrl = s3Connection.uploadAudio(file, login);
            contentService.createAudio(photoUrl, user.get().getId());
            return ResponseEntity.ok("Audio was uploaded to user: " + login);
        } else {
            return ResponseEntity.ok("User with login: " + login + " not found");
        }
    }
}


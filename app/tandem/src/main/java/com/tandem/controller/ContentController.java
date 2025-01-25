package com.tandem.controller;

import com.tandem.model.dto.AudioDTO;
import com.tandem.model.dto.PhotoDTO;
import com.tandem.model.dto.UpdateUserDTO;
import com.tandem.model.dto.VideoDTO;
import com.tandem.model.entity.AudioEntity;
import com.tandem.model.entity.PhotoEntity;
import com.tandem.model.entity.UserEntity;
import com.tandem.model.entity.VideoEntity;
import com.tandem.service.content.IContentService;
import com.tandem.service.s3.IS3Connection;
import com.tandem.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    @Autowired
    private IContentService contentService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IS3Connection s3Connection;

    @GetMapping("/get_all_photos_by_id/{log}")
    public ResponseEntity<?> getAllPhotosByUserLogin(@PathVariable String log) {
        if (log == null || log.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login cannot be empty or blank.");
        }

        Optional<UserEntity> user = userService.findByLogin(log);
        if (user.isPresent()) {
            List<PhotoEntity> photos = contentService.getPhotosByUser(user.get().getId());
            if (!photos.isEmpty()) {
                return ResponseEntity.ok(wrapPhotList(photos));
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
            List<VideoEntity> videos = contentService.getVideosByUser(user.get().getId());
            if (!videos.isEmpty()) {
                return ResponseEntity.ok(wrapVideoList(videos));
            } else {
                return ResponseEntity.ok("User: " + log + " not have videos");
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
            List<AudioEntity> audios = contentService.getAudiosByUser(user.get().getId());
            if (!audios.isEmpty()) {
                return ResponseEntity.ok(wrapAudioList(audios));
            } else {
                return ResponseEntity.ok("User: " + log + " not have audios");
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

        PhotoEntity photo = contentService.getPhotoById(id);
        if (photo == null) {
            return ResponseEntity.ok("Photo with id: " + id + " not found");
        }

        s3Connection.deletePhoto(photo.getPhotoUrl());
        contentService.deletePhoto(id);
        return ResponseEntity.ok("Photo with id: " + id + " was deleted");
    }

    @DeleteMapping("/delete_video/{id}")
    public ResponseEntity<String> deleteVideoById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content id can't be negative");
        }

        VideoEntity video = contentService.getVideoById(id);
        if (video == null) {
            return ResponseEntity.ok("Video with id: " + id + " not found");
        }

        s3Connection.deleteVideo(video.getVideoUrl());
        contentService.deleteVideo(id);
        return ResponseEntity.ok("Video with id: " + id + " was deleted");
    }

    @DeleteMapping("/delete_text/{id}")
    public ResponseEntity<String> deleteTextById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content id can't be negative");
        }

        contentService.deleteTextContent(id);
        return ResponseEntity.ok("Text with id: " + id + " was deleted");
    }

    @DeleteMapping("/delete_audio/{id}")
    public ResponseEntity<String> deleteAudioById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content id can't be negative");
        }

        AudioEntity audio = contentService.getAudioById(id);
        if (audio == null) {
            return ResponseEntity.ok("Audio with id: " + id + " was not found");
        }

        s3Connection.deleteAudio(audio.getAudioUrl());
        contentService.deleteAudio(id);
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

    @GetMapping("/get_photo_by_id/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable Long id) {
        PhotoEntity photo = contentService.getPhotoById(id);
        if (photo == null) {
            return ResponseEntity.ok("Photo with id: " + id + " not found");
        } else {
            return ResponseEntity.ok(wrapPhoto(photo));
        }
    }

    @PutMapping("/change_icon/{login}")
    public ResponseEntity<String> changeUserIcon(@PathVariable String login,
                                                 @RequestParam("file") MultipartFile file) {
        if (login.isEmpty() || file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User login or file can't be empty");
        }

        Optional<UserEntity> user = userService.findByLogin(login);
        if (user.isPresent()) {
            s3Connection.deleteUserIcon(login);
            String iconUrl = s3Connection.changeUserIcon(file, login);

            UpdateUserDTO userDTO = new UpdateUserDTO(user.get().getUsername(), user.get().getAbout(), iconUrl);
            userService.updateUserByLogin(userDTO, login);
            return ResponseEntity.ok("Icon for user: " + login + " was change successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with login: " + login + " not found");
        }
    }

    @GetMapping("/get_audio_by_id/{id}")
    public ResponseEntity<?> getAudioById(@PathVariable Long id) {
        AudioEntity audio = contentService.getAudioById(id);
        if (audio != null) {
            return ResponseEntity.ok(wrapAudio(audio));
        } else {
            return ResponseEntity.ok("Audio with id: " + id + " not found");
        }
    }

    @GetMapping("/get_video_by_id/{id}")
    public ResponseEntity<?> getVideoById(@PathVariable Long id) {
        VideoEntity video = contentService.getVideoById(id);
        if (video != null) {
            return ResponseEntity.ok(wrapVideo(video));
        } else {
            return ResponseEntity.ok("Video with id: " + id + " not found");
        }
    }

    private PhotoDTO wrapPhoto(PhotoEntity photo) {
        return new PhotoDTO(
                photo.getPhotoId(),
                photo.getPhotoUrl(),
                photo.getDescription(),
                photo.getPostAt(),
                photo.getUserId()
        );
    }

    private AudioDTO wrapAudio(AudioEntity audio) {
        return new AudioDTO(
                audio.getAudioId(),
                audio.getAudioUrl(),
                audio.getPostAt(),
                audio.getUserId()
        );
    }

    private VideoDTO wrapVideo(VideoEntity video) {
        return new VideoDTO(
                video.getVideoId(),
                video.getVideoUrl(),
                video.getDescription(),
                video.getPostAt(),
                video.getUserId()
        );
    }

    private List<VideoDTO> wrapVideoList(List<VideoEntity> videoEntities) {
        return videoEntities.stream()
                .map(this::wrapVideo)
                .collect(Collectors.toList());
    }

    private List<AudioDTO> wrapAudioList(List<AudioEntity> audioEntities) {
        return audioEntities.stream()
                .map(this::wrapAudio)
                .collect(Collectors.toList());
    }

    private List<PhotoDTO> wrapPhotList(List<PhotoEntity> photoEntities) {
        return photoEntities.stream()
                .map(this::wrapPhoto)
                .collect(Collectors.toList());
    }
}
package com.tandem.service.s3;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface IS3Connection {
    void createUserFolder(String login);
    void uploadUserIcon(File file, String login);
    void deleteUserIcon(String login);
    void changeUserIcon(File file, String login);
    void uploadPhoto(File file, String login);
    void uploadVideo(File file, String login);
    void uploadAudio(File file, String login);
    void deletePhoto(File file, String login);
    void deleteAudio(File file, String login);
    void deleteVideo(File file, String login);
    List<String> getAllUserPhotos(String login);
    List<String> getAllUserVideos(String login);
    List<String> getAllUserAudios(String login);
}

package com.tandem.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Connection implements IS3Connection {

    @Autowired
    private final AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public S3Connection(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public void createUserFolder(String login) {
        String folderPath = login + "/";

        if (!amazonS3.doesObjectExist(bucketName, folderPath)) {
            byte[] emptyContent = new byte[0];
            InputStream emptyStream = new ByteArrayInputStream(emptyContent);
            ObjectMetadata metadata = new ObjectMetadata();

            amazonS3.putObject(bucketName, folderPath + "dummy", emptyStream, metadata);
            System.out.println("Created folder: " + folderPath);
        }
    }

    @Override
    public void uploadUserIcon(File file, String login) {
        String key = login + "/icons/" + file.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
        System.out.println("Uploaded user icon: " + key);
    }

    @Override
    public void deleteUserIcon(String login) {
        String key = login + "/icons/";
        ObjectListing objectListing = amazonS3.listObjects(bucketName, key);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            amazonS3.deleteObject(bucketName, objectSummary.getKey());
            System.out.println("Deleted user icon: " + objectSummary.getKey());
        }
    }

    @Override
    public void changeUserIcon(File file, String login) {
        deleteUserIcon(login);
        uploadUserIcon(file, login);
    }

    @Override
    public void uploadPhoto(File file, String login) {
        String key = login + "/photos/" + file.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
        System.out.println("Uploaded photo: " + key);
    }

    @Override
    public void uploadVideo(File file, String login) {
        String key = login + "/videos/" + file.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
        System.out.println("Uploaded video: " + key);
    }

    @Override
    public void uploadAudio(File file, String login) {
        String key = login + "/audios/" + file.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
        System.out.println("Uploaded audio: " + key);
    }

    @Override
    public void deletePhoto(File file, String login) {
        String key = login + "/photos/" + file.getName();
        amazonS3.deleteObject(bucketName, key);
        System.out.println("Deleted photo: " + key);
    }

    @Override
    public void deleteAudio(File file, String login) {
        String key = login + "/audios/" + file.getName();
        amazonS3.deleteObject(bucketName, key);
        System.out.println("Deleted audio: " + key);
    }

    @Override
    public void deleteVideo(File file, String login) {
        String key = login + "/videos/" + file.getName();
        amazonS3.deleteObject(bucketName, key);
        System.out.println("Deleted video: " + key);
    }

    @Override
    public List<String> getAllUserPhotos(String login) {
        List<String> photoUrls = new ArrayList<>();
        String prefix = login + "/photos/";
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            photoUrls.add(amazonS3.getUrl(bucketName, objectSummary.getKey()).toString());
        }
        return photoUrls;
    }

    @Override
    public List<String> getAllUserVideos(String login) {
        List<String> videoUrls = new ArrayList<>();
        String prefix = login + "/videos/";
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            videoUrls.add(amazonS3.getUrl(bucketName, objectSummary.getKey()).toString());
        }
        return videoUrls;
    }

    @Override
    public List<String> getAllUserAudios(String login) {
        List<String> audioUrls = new ArrayList<>();
        String prefix = login + "/audios/";
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            audioUrls.add(amazonS3.getUrl(bucketName, objectSummary.getKey()).toString());
        }
        return audioUrls;
    }
}

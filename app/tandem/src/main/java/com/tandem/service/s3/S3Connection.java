package com.tandem.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Connection implements IS3Connection {

    @Autowired
    private final AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String bucketName;
    private final Logger log = LoggerFactory.getLogger(S3Connection.class);

    public S3Connection(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public void createUserFolder(String login) {
        String folderPath = "users/" + login + "/";

        if (!amazonS3.doesObjectExist(bucketName, folderPath)) {
            byte[] emptyContent = new byte[0];
            InputStream emptyStream = new ByteArrayInputStream(emptyContent);
            ObjectMetadata metadata = new ObjectMetadata();

            amazonS3.putObject(bucketName, folderPath + "dummy", emptyStream, metadata);
            log.info("Created user folder: {}", folderPath);
        }
    }

    @Override
    public String uploadUserIcon(File file, String login) {
        String key = "users/" + login + "/icons/" + file.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
        log.info("Uploaded user icon: {}", key);
        return amazonS3.getUrl(bucketName, key).toString();
    }

    @Override
    public void deleteUserIcon(String login) {
        String keyPrefix = "users/" + login + "/icons/";
        deleteObjectsWithPrefix(keyPrefix);
    }

    @Override
    public void deleteUser(String login) {
        try {
            deleteUserIcon(login);
            deleteUserFiles(login, "/photos/");
            deleteUserFiles(login, "/videos/");
            deleteUserFiles(login, "/audios/");

            String folderPath = "users/" + login + "/";
            deleteObjectsWithPrefix(folderPath);

            log.info("User and their resources deleted successfully: {}", login);
        } catch (Exception e) {
            log.error("Error occurred while deleting user: {}", login, e);
        }
    }

    @Override
    public String changeUserIcon(MultipartFile file, String login) {
        String key = "users/" + login + "/icons/" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));
            log.info("Uploaded new user icon: {}", key);
            return amazonS3.getUrl(bucketName, key).toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload user icon: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public String uploadPhoto(MultipartFile file, String login) {
        return uploadFileToFolder(file, "users/" + login + "/photos/");
    }

    @Override
    public String uploadVideo(MultipartFile file, String login) {
        return uploadFileToFolder(file, "users/" + login + "/videos/");
    }

    @Override
    public String uploadAudio(MultipartFile file, String login) {
        return uploadFileToFolder(file, "users/" + login + "/audios/");
    }

    @Override
    public void deletePhoto(String url) {
        deleteFileByUrl(url);
    }

    @Override
    public void deleteAudio(String url) {
        deleteFileByUrl(url);
    }

    @Override
    public void deleteVideo(String url) {
        deleteFileByUrl(url);
    }

    @Override
    public List<String> getAllUserPhotos(String login) {
        return listFilesWithPrefix("users/" + login + "/photos/");
    }

    @Override
    public List<String> getAllUserVideos(String login) {
        return listFilesWithPrefix("users/" + login + "/videos/");
    }

    @Override
    public List<String> getAllUserAudios(String login) {
        return listFilesWithPrefix("users/" + login + "/audios/");
    }

    @Override
    public void createGroup(String groupName) {
        String folderPath = "groups/" + groupName + "/";

        if (!amazonS3.doesObjectExist(bucketName, folderPath)) {
            byte[] emptyContent = new byte[0];
            InputStream emptyStream = new ByteArrayInputStream(emptyContent);
            ObjectMetadata metadata = new ObjectMetadata();

            amazonS3.putObject(bucketName, folderPath + "dummy", emptyStream, metadata);
            log.info("Created group folder: {}", folderPath);
        }
    }

    @Override
    public void deleteGroup(String groupName) {
        String folderPath = "groups/" + groupName + "/";
        deleteObjectsWithPrefix(folderPath);
        log.info("Deleted group folder: {}", folderPath);
    }

    @Override
    public String uploadGroupLogo(File file, String groupName) {
        String key = "groups/" + groupName + "/logo/" + file.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, key, file));
        log.info("Uploaded group logo: {}", key);
        return amazonS3.getUrl(bucketName, key).toString();
    }

    private String uploadFileToFolder(MultipartFile file, String folderPath) {
        String fileName = file.getOriginalFilename();
        String key = folderPath + fileName;

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));
            log.info("Uploaded file: {}", key);

            return amazonS3.getUrl(bucketName, key).toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + fileName, e);
        }
    }

    private List<String> listFilesWithPrefix(String prefix) {
        List<String> fileUrls = new ArrayList<>();
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            fileUrls.add(amazonS3.getUrl(bucketName, objectSummary.getKey()).toString());
        }
        return fileUrls;
    }

    private void deleteObjectsWithPrefix(String prefix) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            amazonS3.deleteObject(bucketName, objectSummary.getKey());
            log.info("Deleted file: {}", objectSummary.getKey());
        }
    }

    private void deleteFileByUrl(String url) {
        String key = extractKeyFromUrl(url);
        if (key != null) {
            amazonS3.deleteObject(bucketName, key);
            log.info("Deleted file: {}", key);
        } else {
            log.warn("Invalid URL or key extraction failed: {}", url);
        }
    }

    private String extractKeyFromUrl(String url) {
        if (url != null && url.contains(bucketName)) {
            int startIndex = url.indexOf(bucketName) + bucketName.length() + 1;
            if (startIndex >= 0 && startIndex < url.length()) {
                return url.substring(startIndex);
            }
        }
        return null;
    }

    private void deleteUserFiles(String login, String folderSuffix) {
        String folderPath = "users/" + login + folderSuffix;
        ObjectListing objectListing = amazonS3.listObjects(bucketName, folderPath);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            amazonS3.deleteObject(bucketName, objectSummary.getKey());
            log.info("Deleted file: {}", objectSummary.getKey());
        }
    }
}
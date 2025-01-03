package com.tandem.service.photo;

import com.tandem.model.entity.PhotoEntity;
import com.tandem.repository.PhotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService implements IPhotoService {

    @Autowired
    private PhotoRepository photoRepository;
    private final Logger logger = LoggerFactory.getLogger(PhotoService.class);

    @Override
    public void savePhoto(PhotoEntity photo) {
        try {
            logger.info("Saving photo: {}", photo);
            photoRepository.save(photo);
        } catch (Exception e) {
            logger.error("Error while saving photo: {}", photo, e);
            throw new RuntimeException("Error while saving photo", e);
        }
    }

    @Override
    public void deletePhotoById(Long photoId) {
        try {
            logger.info("Deleting photo with ID: {}", photoId);
            photoRepository.deleteById(photoId);
        } catch (Exception e) {
            logger.error("Error while deleting photo with ID: {}", photoId, e);
            throw new RuntimeException("Error while deleting photo", e);
        }
    }

    @Override
    public List<PhotoEntity> getAllPhotos() {
        logger.info("Fetching all photos.");
        return photoRepository.findAll();
    }

    @Override
    public List<PhotoEntity> getAllPhotosSentByUser(Long userId) {
        logger.info("Fetching all photos sent by user with ID: {}", userId);
        return photoRepository.getAllPhotosByUser(userId);
    }
}

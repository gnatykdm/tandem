package com.tandem.service.photo;

import com.tandem.model.entity.PhotoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPhotoService {
    void savePhoto(PhotoEntity photo);
    void deletePhotoById(Long photoId);
    List<PhotoEntity> getAllPhotos();
    List<PhotoEntity> getAllPhotosSentByUser(Long userId);
}

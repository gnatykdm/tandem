package com.tandem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Photo", schema = "content_management")
public class PhotoEntity {

    @Id
    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "post_at")
    private LocalDateTime postAt;

    @Column(name = "user_id")
    private Long userId;

    public PhotoEntity(String photoUrl, String description, Long userId) {
        this.photoUrl = photoUrl;
        this.description = description;
        this.userId = userId;
        this.postAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PhotoEntity photo = (PhotoEntity) object;
        return Objects.equals(photoId, photo.photoId) && Objects.equals(photoUrl, photo.photoUrl) && Objects.equals(description, photo.description) && Objects.equals(postAt, photo.postAt) && Objects.equals(userId, photo.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, photoUrl, description, postAt, userId);
    }

    @Override
    public String toString() {
        return "PhotoEntity{" +
                "photoId=" + photoId +
                ", photoUrl='" + photoUrl + '\'' +
                ", description='" + description + '\'' +
                ", postAt=" + postAt +
                ", userId=" + userId +
                '}';
    }
}

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
@Table(name = "photo", schema = "content_management")
public class PhotoEntity {
    @Id
    @Column(name = "photo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "post_at", nullable = false)
    private LocalDateTime postAt = LocalDateTime.now();

    public PhotoEntity(String photoUrl, String description) {
        this.photoUrl = photoUrl;
        this.description = description;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PhotoEntity that = (PhotoEntity) object;
        return Objects.equals(photoId, that.photoId) && Objects.equals(photoUrl, that.photoUrl) && Objects.equals(description, that.description) && Objects.equals(postAt, that.postAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, photoUrl, description, postAt);
    }

    @Override
    public String toString() {
        return "PhotoEntity{" +
                "photoId=" + photoId +
                ", photoUrl='" + photoUrl + '\'' +
                ", description='" + description + '\'' +
                ", postAt=" + postAt +
                '}';
    }
}

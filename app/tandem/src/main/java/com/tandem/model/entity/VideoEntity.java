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
@Table(name = "video", schema = "content_management")
public class VideoEntity {
    @Id
    @Column(name = "video_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "post_at", nullable = false)
    private LocalDateTime postAt = LocalDateTime.now();

    @Column(name = "user_id")
    private Long userId;

    public VideoEntity(String videoUrl, String description) {
        this.videoUrl = videoUrl;
        this.description = description;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        VideoEntity that = (VideoEntity) object;
        return Objects.equals(videoId, that.videoId) && Objects.equals(videoUrl, that.videoUrl) && Objects.equals(description, that.description) && Objects.equals(postAt, that.postAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, videoUrl, description, postAt);
    }

    @Override
    public String toString() {
        return "VideoEntity{" +
                "videoId=" + videoId +
                ", videoUrl='" + videoUrl + '\'' +
                ", description='" + description + '\'' +
                ", postAt=" + postAt +
                '}';
    }
}


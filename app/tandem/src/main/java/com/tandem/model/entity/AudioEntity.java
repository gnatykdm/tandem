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
@Table(name = "Audio", schema = "content_management")
public class AudioEntity {
    @Id
    @Column(name = "audio_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long audioId;

    @Column(name = "audio_url", nullable = false)
    private String audioUrl;

    @Column(name = "post_at", nullable = false)
    private LocalDateTime postAt = LocalDateTime.now();

    @Column(name = "user_id")
    private Long userId;

    public AudioEntity(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AudioEntity that = (AudioEntity) object;
        return Objects.equals(audioId, that.audioId) && Objects.equals(audioUrl, that.audioUrl) && Objects.equals(postAt, that.postAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(audioId, audioUrl, postAt);
    }

    @Override
    public String toString() {
        return "AudioEntity{" +
                "audioId=" + audioId +
                ", audioUrl='" + audioUrl + '\'' +
                ", postAt=" + postAt +
                '}';
    }
}


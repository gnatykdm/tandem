package com.tandem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Content")
public class ContentEntity {
    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "photo")
    private PhotoEntity photo;

    @ManyToOne
    @JoinColumn(name = "video")
    private VideoEntity video;

    @ManyToOne
    @JoinColumn(name = "audio")
    private AudioEntity audio;

    @ManyToOne
    @JoinColumn(name = "text")
    private TextEntity text;

    public ContentEntity(PhotoEntity photo, VideoEntity video, AudioEntity audio, TextEntity text) {
        this.photo = photo;
        this.video = video;
        this.audio = audio;
        this.text = text;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ContentEntity that = (ContentEntity) object;
        return Objects.equals(contentId, that.contentId) && Objects.equals(photo, that.photo) && Objects.equals(video, that.video) && Objects.equals(audio, that.audio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId, photo, video, audio);
    }

    @Override
    public String toString() {
        return "ContentEntity{" +
                "contentId=" + contentId +
                ", photo=" + photo +
                ", video=" + video +
                ", audio=" + audio +
                '}';
    }
}

package com.tandem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "video")
public class VideoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long videoId;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "post_at")
    private LocalDateTime postAt = LocalDateTime.now();
}

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
@Table(name = "audio")
public class AudioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audio_id")
    private Long audioId;

    @Column(name = "audio_url", nullable = false)
    private String audioUrl;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "post_at")
    private LocalDateTime postAt = LocalDateTime.now();
}

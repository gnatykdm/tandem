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
@Table(name = "photo")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "post_at")
    private LocalDateTime postAt = LocalDateTime.now();
}

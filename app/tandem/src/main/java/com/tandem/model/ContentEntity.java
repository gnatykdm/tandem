package com.tandem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "content")
public class ContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "photo", referencedColumnName = "photo_id")
    private PhotoEntity photo;

    @ManyToOne
    @JoinColumn(name = "video", referencedColumnName = "video_id")
    private VideoEntity video;

    @ManyToOne
    @JoinColumn(name = "audio", referencedColumnName = "audio_id")
    private AudioEntity audio;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;
}

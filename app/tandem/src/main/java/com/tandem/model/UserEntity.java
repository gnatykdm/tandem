package com.tandem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "login", unique = true, nullable = false, length = 32)
    private String userLogin;

    @Column(name = "username", nullable = false, length = 64)
    private String userName;

    @Column(name = "email", nullable = false, unique = true, length = 64)
    private String userEmail;

    @Column(name = "password", nullable = false)
    private String userPassword;

    @Column(name = "key", nullable = false, unique = true, length = 32)
    private String userKey;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContentEntity> userContent;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<GroupEntity> userGroups;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FollowerEntity> followers;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FollowerEntity> following;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> receivedMessages;

    @Column(name = "about")
    private String userAbout;

    @Column(name = "register_date", nullable = false)
    private LocalDateTime userRegisterDate = LocalDateTime.now();

    @Column(name = "profile_image")
    private String userProfileImg;
}

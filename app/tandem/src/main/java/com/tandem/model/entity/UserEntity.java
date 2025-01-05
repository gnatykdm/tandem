package com.tandem.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.Objects;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "\"User\"", schema = "user_management")
public class UserEntity  {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false, length = 32)
    private String login;

    @Column(name = "username", nullable = false, length = 63)
    private String username;

    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @Column(name = "password", nullable = false, length = 32)
    private String password;

    @Column(name = "about")
    private String about;

    @Column(name = "profile_image")
    private String profileImage;

    @OneToMany(mappedBy = "senderMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> messages;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FollowsEntity> followers;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FollowsEntity> following;

    public UserEntity(String login, String username, String email, String password,
                      String about, String profileImage) {
        this.login = login;
        this.username = username;
        this.email = email;
        this.password = password;
        this.about = about;
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserEntity that = (UserEntity) object;
        return Objects.equals(id, that.id) && Objects.equals(login, that.login) &&
                Objects.equals(username, that.username) && Objects.equals(email, that.email)
                && Objects.equals(password, that.password)
                && Objects.equals(about, that.about) && Objects.equals(profileImage, that.profileImage)
                && Objects.equals(messages, that.messages) && Objects.equals(followers, that.followers)
                && Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, username, email, password,
                about, profileImage, messages, followers, following);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", about='" + about + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", messages=" + messages +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }
}


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
@Table(name = "follows")
public class FollowsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "followers", nullable = false)
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "following", nullable = false)
    private UserEntity following;

    public FollowsEntity(UserEntity follower, UserEntity following) {
        this.follower = follower;
        this.following = following;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        FollowsEntity that = (FollowsEntity) object;
        return Objects.equals(id, that.id) && Objects.equals(follower, that.follower) && Objects.equals(following, that.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, follower, following);
    }

    @Override
    public String toString() {
        return "FollowsEntity{" +
                "id=" + id +
                ", follower=" + follower +
                ", following=" + following +
                '}';
    }
}


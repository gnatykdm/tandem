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
@Table(name = "Optional")
public class Optional {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @Column(name = "admin", nullable = false)
    private Boolean admin = false;

    public Optional(UserEntity user, GroupEntity group, Boolean admin) {
        this.user = user;
        this.group = group;
        this.admin = admin;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Optional optional = (Optional) object;
        return Objects.equals(id, optional.id) && Objects.equals(user, optional.user) && Objects.equals(group, optional.group) && Objects.equals(admin, optional.admin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, group, admin);
    }

    @Override
    public String toString() {
        return "Optional{" +
                "id=" + id +
                ", user=" + user +
                ", group=" + group +
                ", admin=" + admin +
                '}';
    }
}

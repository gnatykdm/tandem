package com.tandem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "\"Group\"", schema = "group_management")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_name", nullable = false, length = 32)
    private String groupName;

    @Column(name = "group_icon", nullable = false)
    private String groupIcon;

    @Column(name = "group_description")
    private String groupDescription;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToMany(mappedBy = "groups")
    private Set<UserEntity> users = new HashSet<>();

    @Column(name = "access_code", length = 10)
    private String accessCode;

    @Column(name = "type", nullable = false)
    private Boolean type;

    public GroupEntity(String groupName, String groupIcon,
                       String groupDescription, LocalDateTime creationDate,
                       String accessCode, Boolean type) {
        this.groupName = groupName;
        this.groupIcon = groupIcon;
        this.groupDescription = groupDescription;
        this.creationDate = creationDate;
        this.accessCode = accessCode;
        this.type = type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GroupEntity that = (GroupEntity) object;
        return Objects.equals(groupId, that.groupId) && Objects.equals(groupName, that.groupName) &&
                Objects.equals(groupIcon, that.groupIcon) && Objects.equals(groupDescription, that.groupDescription)
                && Objects.equals(creationDate, that.creationDate) && Objects.equals(accessCode, that.accessCode)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, groupIcon, groupDescription, creationDate, accessCode, type);
    }

    @Override
    public String toString() {
        return "GroupEntity{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", groupIcon='" + groupIcon + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", creationDate=" + creationDate +
                ", accessCode='" + accessCode + '\'' +
                ", type=" + type +
                '}';
    }
}

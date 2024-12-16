package com.tandem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Group")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(name = "group_name", nullable = false, length = 32)
    private String groupName;

    @Column(name = "group_key", nullable = false, length = 32)
    private String groupKey;

    @Column(name = "group_icon", nullable = false)
    private String groupIcon;

    @Column(name = "group_description")
    private String groupDescription;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "messages")
    private MessageEntity messages;

    @Column(name = "access_code", length = 10)
    private String accessCode;

    @Column(nullable = false)
    private Boolean type;

    public GroupEntity(String groupName, String groupKey, String groupIcon,
                       String groupDescription, LocalDateTime creationDate,
                       MessageEntity messages,
                       String accessCode, Boolean type) {
        this.groupName = groupName;
        this.groupKey = groupKey;
        this.groupIcon = groupIcon;
        this.groupDescription = groupDescription;
        this.creationDate = creationDate;
        this.messages = messages;
        this.accessCode = accessCode;
        this.type = type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GroupEntity that = (GroupEntity) object;
        return Objects.equals(groupId, that.groupId) && Objects.equals(groupName, that.groupName) && Objects.equals(groupKey, that.groupKey) && Objects.equals(groupIcon, that.groupIcon) && Objects.equals(groupDescription, that.groupDescription) && Objects.equals(creationDate, that.creationDate) && Objects.equals(messages, that.messages) && Objects.equals(accessCode, that.accessCode) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, groupKey, groupIcon, groupDescription, creationDate, messages, accessCode, type);
    }

    @Override
    public String toString() {
        return "GroupEntity{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", groupKey='" + groupKey + '\'' +
                ", groupIcon='" + groupIcon + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", creationDate=" + creationDate +
                ", messages=" + messages +
                ", accessCode='" + accessCode + '\'' +
                ", type=" + type +
                '}';
    }
}

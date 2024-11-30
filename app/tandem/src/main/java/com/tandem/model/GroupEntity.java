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
@Table(name = "group")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_name", nullable = false, length = 32)
    private String groupName;

    @Column(name = "group_key", nullable = false, length = 32)
    private String groupKey;

    @Column(name = "group_icon")
    private String groupIcon;

    @Column(name = "group_description")
    private String groupDescription;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToMany(mappedBy = "userGroups")
    private List<UserEntity> members;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContentEntity> groupContent;
}

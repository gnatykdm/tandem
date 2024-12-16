package com.tandem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowsDTO {
    private Long id;
    private Long followerId;
    private String followerUsername;
    private Long followingId;
    private String followingUsername;
}

package com.tandem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {
    private Long groupId;
    private String groupName;
    private String groupIcon;
    private String groupDescription;
    private LocalDateTime creationDate;
    private String accessCode;
    private Boolean type;
}

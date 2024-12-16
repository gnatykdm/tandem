package com.tandem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionalDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long groupId;
    private String groupName;
    private Boolean isAdmin;
}

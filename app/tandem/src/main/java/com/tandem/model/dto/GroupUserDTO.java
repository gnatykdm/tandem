package com.tandem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupUserDTO {
    private Long id;
    private String username;
    private Boolean admin;
}

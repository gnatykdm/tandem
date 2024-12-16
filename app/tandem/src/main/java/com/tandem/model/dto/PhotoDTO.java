package com.tandem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {
    private Long photoId;
    private String photoUrl;
    private String description;
    private LocalDateTime postAt;
}

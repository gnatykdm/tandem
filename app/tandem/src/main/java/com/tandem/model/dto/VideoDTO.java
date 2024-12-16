package com.tandem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDTO {
    private Long videoId;
    private String videoUrl;
    private String description;
    private LocalDateTime postAt;
}

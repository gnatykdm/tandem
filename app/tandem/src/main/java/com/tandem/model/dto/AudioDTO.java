package com.tandem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioDTO {
    private Long audioId;
    private String audioUrl;
    private LocalDateTime postAt;
}

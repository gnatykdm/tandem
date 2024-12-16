package com.tandem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDTO {
    private Long contentId;
    private PhotoDTO photo;
    private VideoDTO video;
    private AudioDTO audio;
}

package com.tandem.model.dto;

import com.tandem.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long messageId;
    private String senderName;
    private String content;
    private LocalDateTime sendAt;
}


package com.tandem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long messageId;
    private Long senderId;
    private String senderUsername;
    private String content;
    private LocalDateTime sendAt;
}


package com.tandem.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "\"Message\"", schema = "message_management")
public class MessageEntity {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private UserEntity senderMessage;

    @Column(name = "content", nullable = false)
    private String contentMessage;

    @Column(name = "send_at", nullable = false)
    private LocalDateTime sendAt = LocalDateTime.now();

    public MessageEntity(UserEntity sender, String content) {
        this.senderMessage = sender;
        this.contentMessage = content;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MessageEntity that = (MessageEntity) object;
        return Objects.equals(messageId, that.messageId) && Objects.equals(senderMessage, that.senderMessage) && Objects.equals(contentMessage, that.contentMessage) && Objects.equals(sendAt, that.sendAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, senderMessage, contentMessage, sendAt);
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "messageId=" + messageId +
                ", sender=" + senderMessage +
                ", content='" + contentMessage + '\'' +
                ", sendAt=" + sendAt +
                '}';
    }
}

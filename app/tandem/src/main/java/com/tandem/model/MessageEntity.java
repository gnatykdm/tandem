package com.tandem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private UserEntity sender;

    @Column(nullable = false)
    private String content;

    @Column(name = "send_at", nullable = false)
    private LocalDateTime sendAt = LocalDateTime.now();

    public MessageEntity(UserEntity sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MessageEntity that = (MessageEntity) object;
        return Objects.equals(messageId, that.messageId) && Objects.equals(sender, that.sender) && Objects.equals(content, that.content) && Objects.equals(sendAt, that.sendAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, sender, content, sendAt);
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "messageId=" + messageId +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", sendAt=" + sendAt +
                '}';
    }
}

package com.tandem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "text", schema = "content_management")
public class TextEntity {
    @Id
    @Column(name = "text_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long textId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "post_at", nullable = false)
    private LocalDateTime postAt = LocalDateTime.now();

    public TextEntity(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TextEntity that = (TextEntity) object;
        return Objects.equals(textId, that.textId) && Objects.equals(content, that.content) && Objects.equals(postAt, that.postAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textId, content, postAt);
    }

    @Override
    public String toString() {
        return "TextEntity{" +
                "textId=" + textId +
                ", content='" + content + '\'' +
                ", postAt=" + postAt +
                '}';
    }
}

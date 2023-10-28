package org.project.model;

import lombok.*;
import org.project.util.enums.UserMessageType;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "user_message")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "user_message")
public class UserMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "message_id", nullable = false)
    private long messageId;
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Column(name = "formatting_type", nullable = false)
    @Enumerated(value = STRING)
    private UserMessageType formattingType;
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}

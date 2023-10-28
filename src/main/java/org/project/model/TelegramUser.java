package org.project.model;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.MERGE;

@Entity
@Table(name = "telegram_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "telegram_user")
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "telegram_id", unique = true)
    private long telegramId;
    @OneToOne(cascade = MERGE)
    @JoinColumn(name = "telegram_id", insertable = false, updatable = false)
    private Driver driver;

    public boolean isDriver() {
        return driver != null;
    }
}

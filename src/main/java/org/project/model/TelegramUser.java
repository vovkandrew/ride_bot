package org.project.model;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

import static java.util.Optional.ofNullable;
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
    @OneToOne(cascade = MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "telegram_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Driver driver;

    public boolean isDriver() {
        return ofNullable(driver).isPresent();
    }
}

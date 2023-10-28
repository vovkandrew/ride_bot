package org.project.model;

import lombok.*;
import org.project.util.enums.HandlerName;

import javax.persistence.*;
import java.util.Objects;

import static java.util.Objects.hash;
import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "phase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "phase")
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "handler_name", unique = true, nullable = false)
    @Enumerated(value = STRING)
    private HandlerName handlerName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phase phase = (Phase) o;
        return getId() == phase.getId() && Objects.equals(getHandlerName(), phase.getHandlerName());
    }

    @Override
    public int hashCode() {
        return hash(getId(), getHandlerName());
    }
}

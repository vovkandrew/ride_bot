package org.project.model;

import lombok.*;

import javax.persistence.*;

import java.util.Optional;

import static javax.persistence.CascadeType.MERGE;

@Entity
@Table(name = "user_phase")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "user_phase")
public class UserPhase {
    @Id
    private long userId;
    @ManyToOne(cascade = MERGE)
    @JoinColumn(name = "phase_id")
    private Phase phase;

    public boolean isPhaseEquals(Phase handlerPhase) {
        return Optional.ofNullable(phase).isPresent() && phase.equals(handlerPhase);
    }
}

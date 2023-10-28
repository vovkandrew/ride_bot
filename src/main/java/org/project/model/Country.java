package org.project.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "country")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "country")
public class Country {
    @Id
    private long id;
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    @OneToMany(fetch = LAZY)
    private Set<City> cities = new HashSet<>();
}

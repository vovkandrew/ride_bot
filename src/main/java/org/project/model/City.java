package org.project.model;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.MERGE;

@Entity
@Table(name = "city")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "city")
public class City {
    @Id
    private long id;
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @ManyToOne(cascade = MERGE)
    @JoinColumn(name = "country_id")
    private Country country;
}

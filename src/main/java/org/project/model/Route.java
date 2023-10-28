package org.project.model;

import lombok.*;
import org.project.util.enums.Status;
import org.project.util.enums.UserType;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.EnumType.STRING;
import static org.project.util.constants.Constants.EMPTY_STRING;

@Entity
@Table(name = "route")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "route")
public class Route implements Formatter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, name = "telegram_user_id")
    private long telegramUserId;
    @ManyToOne(cascade = MERGE)
    @JoinColumn(name = "country_from_id")
    private Country countryFrom;
    @ManyToOne(cascade = MERGE)
    @JoinColumn(name = "city_from_id")
    private City cityFrom;
    @ManyToOne(cascade = MERGE)
    @JoinColumn(name = "country_to_id")
    private Country countryTo;
    @ManyToOne(cascade = MERGE)
    @JoinColumn(name = "city_to_id")
    private City cityTo;
    @Column(name = "status", nullable = false)
    @Enumerated(value = STRING)
    private Status status;
    @Column(name = "user_type", nullable = false)
    @Enumerated(value = STRING)
    private UserType userType;

    @Override
    public Object[] getFormattedData() {
        return new Object[]{Objects.toString(countryFrom.getName(), EMPTY_STRING), Objects.toString(cityFrom.getName(), EMPTY_STRING),
                Objects.toString(countryTo.getName(), EMPTY_STRING), Objects.toString(cityTo.getName(), EMPTY_STRING)};
    }

    public String getSimplifiedRoute() {
        return String.format("%s - %s", cityFrom.getName(), cityTo.getName());
    }
}

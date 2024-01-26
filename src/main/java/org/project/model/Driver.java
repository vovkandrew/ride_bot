package org.project.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;

@Entity
@Table(name = "driver")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "driver")
public class Driver implements Formatter {
    @Id
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "car_model")
    private String carModel;
    @Column(name = "car_color")
    private String carColor;
    @Column(name = "plate_number")
    private String plateNumber;
    @Column(name = "seats_number")
    private int seatsNumber;
    @Column(name = "finished")
    @ColumnDefault(value = "false")
    private boolean finished;
    @OneToMany(cascade = MERGE)
    @JoinColumn(name = "telegram_user_id")
    private List<Route> routes;

    public Object[] getFormattedData() {
        return getFormattedData(firstName, lastName, phoneNumber, carModel, carColor, plateNumber, seatsNumber);
    }

    @Override
    public List<Object> getFormattedDataAsList() {
        return getFormattedDataAsList(firstName, lastName, phoneNumber, carModel, carColor, plateNumber, seatsNumber);
    }
}

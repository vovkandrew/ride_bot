package org.project.model;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.project.util.enums.Status;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "booking")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "booking")
public class Booking implements Formatter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "telegram_user_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private TelegramUser telegramUser;
    @Column(name = "passenger_name")
    private String passengerName;
    @Column(name = "passenger_phone_number")
    private String passengerPhoneNumber;
    @Column(name = "number_of_seats")
    private int numberOfBookedSeats;
    @Column(name = "status", nullable = false)
    @Enumerated(value = STRING)
    private Status status;


    @Override
    public Object[] getFormattedData() {
        return getFormattedData(trip, passengerName, passengerPhoneNumber, numberOfBookedSeats, status);
    }

    @Override
    public List<Object> getFormattedDataAsList() {
        return getFormattedDataAsList(trip, passengerName, passengerPhoneNumber, numberOfBookedSeats, status);
    }
}

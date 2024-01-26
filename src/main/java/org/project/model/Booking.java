package org.project.model;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "booking")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table(value = "booking")
public class Booking {
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

}

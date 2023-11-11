package org.project.model;

import lombok.*;
import org.project.util.enums.Currency;
import org.project.util.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Optional.ofNullable;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static org.project.util.constants.Constants.*;

@Entity
@Table(name = "trip")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@org.springframework.data.relational.core.mapping.Table("trip")
public class Trip implements Formatter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = LAZY, cascade = MERGE)
    @JoinColumn(name = "route_id")
    private Route route;
    @Column(name = "departure_date")
    private LocalDate departureDate;
    @Column(name = "departure_time")
    private LocalTime departureTime;
    @Column(name = "arrival_date")
    private LocalDate arrivalDate;
    @Column(name = "arrival_time")
    private LocalTime arrivalTime;
    @Column(name = "pickup_point")
    private String pickupPoint;
    @Column(name = "dropoff_point")
    private String dropOffPoint;
    @Column(name = "currency")
    @Enumerated(value = STRING)
    private Currency currency;
    @Column(name = "price")
    private int price;
    @Column(name = "baggage_info")
    private String baggageInfo;
    @Column(name = "other_info")
    private String otherInfo;
    @Column(name = "status", nullable = false)
    @Enumerated(value = STRING)
    private Status status;

    public String getFormattedDepartureDate() {
        return Optional.ofNullable(departureDate).isPresent() ? departureDate.format(ofPattern(DATE_FORMAT)) : null;
    }

    public String getFormattedDepartureTime() {
        return ofNullable(departureTime).isPresent() ? departureTime.format(ofPattern(TIME_FORMAT)) : null;
    }

    public String getFormattedArrivalDate() {
        return ofNullable(arrivalDate).isPresent() ? arrivalDate.format(ofPattern(DATE_FORMAT)) : null;
    }

    public String getFormattedArrivalTime() {
        return ofNullable(arrivalTime).isPresent() ? arrivalTime.format(ofPattern(TIME_FORMAT)) : null;
    }

    @Override
    public Object[] getFormattedData() {
        return getFormattedData(route.getSimplifiedRoute(), getFormattedDepartureDate(), getFormattedDepartureTime(),
                getFormattedArrivalDate(), getFormattedArrivalTime(), pickupPoint, dropOffPoint, currency, price,
                baggageInfo, otherInfo);
    }

    public boolean verifyDepartureDate(String userInput) {
        LocalDate userInputDate = LocalDate.parse(userInput, ofPattern(DATE_FORMAT));
        LocalDate todayDate = LocalDate.now();
        return (Optional.ofNullable(arrivalDate).isEmpty() || (arrivalDate.isAfter(userInputDate) ||
                arrivalDate.isEqual(userInputDate))) && (todayDate.isBefore(userInputDate) ||
                todayDate.isEqual(userInputDate));
    }

    public boolean verifyArrivalDate(String userInput) {
        LocalDate userInputDate = LocalDate.parse(userInput, ofPattern(DATE_FORMAT));
        return departureDate.isBefore(userInputDate) || departureDate.isEqual(userInputDate);
    }

    public boolean verifyArrivalTime(String userInput) {
        return !departureDate.isEqual(arrivalDate) || departureTime.isBefore(LocalTime.parse(userInput, ofPattern(TIME_FORMAT)));
    }

    public boolean verifyDepartureTime(String userInput) {
        LocalTime userInputTime = LocalTime.parse(userInput, ofPattern(TIME_FORMAT));
        LocalDate today = LocalDate.now();
        LocalTime leastAllowed = LocalTime.now().plusHours(1);
        boolean isArrivalDateAvailable = ofNullable(arrivalDate).isPresent();

        return (!departureDate.isEqual(today) || userInputTime.isAfter(leastAllowed)) ||
                ((isArrivalDateAvailable && (userInputTime.isBefore(arrivalTime))));
    }
}

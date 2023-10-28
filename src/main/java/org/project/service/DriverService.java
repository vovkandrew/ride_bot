package org.project.service;

import org.project.model.Driver;

import java.util.Optional;

public interface DriverService {
    void deleteDriverById(long userId);

    void deleteDriverByIdAndIsFinished(long userId, boolean finished);

    void saveDriver(Driver driver);

    Driver getDriver(long userId);

    Driver updateLastName(long userId, String lastName);

    Driver updatePhoneNumber(long userId, String phoneNumber);

    Driver updateCarModel(long userId, String carModel);

    Driver updateCarColor(long userId, String carColor);

    Driver updatePlateNumber(long userId, String plateNumber);

    Driver updateSeatsNumber(long userId, String seatsNumber);

    Driver updateFirstName(long userId, String firstName);

    Driver updateFinished(long userId, boolean finished);

    Optional<Driver> findFinishedDriver(long userId);

    Optional<Driver> findUnfinishedDriver(long userId);

    Optional<Driver> findDriver(long userId);
}

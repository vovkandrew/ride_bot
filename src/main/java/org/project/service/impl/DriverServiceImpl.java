package org.project.service.impl;

import lombok.AllArgsConstructor;
import org.project.model.Driver;
import org.project.repository.DriverRepository;
import org.project.service.DriverService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;

    @Override
    public void deleteDriverById(long userId) {
        driverRepository.deleteById(userId);
    }

    @Override
    public void deleteDriverByIdAndIsFinished(long userId, boolean finished) {
        driverRepository.deleteByIdAndFinished(userId, finished);
    }

    @Override
    public void saveDriver(Driver driver) {
        driverRepository.save(driver);
    }

    @Override
    public Driver getDriver(long userId) {
        return driverRepository.getById(userId);
    }

    @Override
    public Driver updateLastName(long userId, String lastName) {
        Driver driver = getDriver(userId);
        driver.setLastName(lastName);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updatePhoneNumber(long userId, String phoneNumber) {
        Driver driver = getDriver(userId);
        driver.setPhoneNumber(phoneNumber);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateCarModel(long userId, String carModel) {
        Driver driver = getDriver(userId);
        driver.setCarModel(carModel);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateCarColor(long userId, String carColor) {
        Driver driver = getDriver(userId);
        driver.setCarColor(carColor);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updatePlateNumber(long userId, String plateNumber) {
        Driver driver = getDriver(userId);
        driver.setPlateNumber(plateNumber);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateSeatsNumber(long userId, String seatsNumber) {
        Driver driver = getDriver(userId);
        driver.setSeatsNumber(Integer.parseInt(seatsNumber));
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateFirstName(long userId, String firstName) {
        Driver driver = getDriver(userId);
        driver.setFirstName(firstName);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateFinished(long userId, boolean finished) {
        Driver driver = driverRepository.getById(userId);
        driver.setFinished(finished);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Optional<Driver> findFinishedDriver(long userId) {
        return driverRepository.findByIdAndFinishedIs(userId, true);
    }

    @Override
    public Optional<Driver> findUnfinishedDriver(long userId) {
        return driverRepository.findByIdAndFinishedIs(userId, false);
    }

    @Override
    public Optional<Driver> findDriver(long userId) {
        return driverRepository.findById(userId);
    }
}

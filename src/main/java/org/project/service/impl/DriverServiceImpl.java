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
    public void deleteDriverById(long telegramUserId) {
        driverRepository.deleteById(telegramUserId);
    }

    @Override
    public void deleteDriverByIdAndIsFinished(long telegramUserId, boolean finished) {
        driverRepository.deleteByIdAndFinished(telegramUserId, finished);
    }

    @Override
    public void saveDriver(Driver driver) {
        driverRepository.save(driver);
    }

    @Override
    public Driver getDriver(long telegramUserId) {
        return driverRepository.getById(telegramUserId);
    }

    @Override
    public Driver updateLastName(long telegramUserId, String lastName) {
        Driver driver = getDriver(telegramUserId);
        driver.setLastName(lastName);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updatePhoneNumber(long telegramUserId, String phoneNumber) {
        Driver driver = getDriver(telegramUserId);
        driver.setPhoneNumber(phoneNumber);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateCarModel(long telegramUserId, String carModel) {
        Driver driver = getDriver(telegramUserId);
        driver.setCarModel(carModel);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateCarColor(long telegramUserId, String carColor) {
        Driver driver = getDriver(telegramUserId);
        driver.setCarColor(carColor);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updatePlateNumber(long telegramUserId, String plateNumber) {
        Driver driver = getDriver(telegramUserId);
        driver.setPlateNumber(plateNumber);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateSeatsNumber(long telegramUserId, String seatsNumber) {
        Driver driver = getDriver(telegramUserId);
        driver.setSeatsNumber(Integer.parseInt(seatsNumber));
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateFirstName(long telegramUserId, String firstName) {
        Driver driver = getDriver(telegramUserId);
        driver.setFirstName(firstName);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Driver updateFinished(long telegramUserId, boolean finished) {
        Driver driver = driverRepository.getById(telegramUserId);
        driver.setFinished(finished);
        saveDriver(driver);
        return driver;
    }

    @Override
    public Optional<Driver> findFinishedDriver(long telegramUserId) {
        return driverRepository.findByIdAndFinishedIs(telegramUserId, true);
    }

    @Override
    public Optional<Driver> findUnfinishedDriver(long telegramUserId) {
        return driverRepository.findByIdAndFinishedIs(telegramUserId, false);
    }

    @Override
    public Optional<Driver> findDriver(long telegramUserId) {
        return driverRepository.findById(telegramUserId);
    }
}

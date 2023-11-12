package org.project.service;

import org.project.model.Trip;

public interface NotificationService {
    void notifyAboutNewTrip(Trip trip);
}

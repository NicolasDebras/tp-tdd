package fr.esgi.cleancode.service;

import java.util.UUID;

public class DrivingLicenceIdGenerationService {

    public UUID generateNewDrivingLicenceId() {
        return UUID.randomUUID();
    }

    public UUID generateNewDrivingLicenceId(String id_social_currency) {
        return UUID.randomUUID();
    }
}

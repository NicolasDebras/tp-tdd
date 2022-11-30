package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
public class CreateNewDrivingLicence {

    private final InMemoryDatabase database;

    public Optional<DrivingLicence> CreateNewDrivingLicence(String id_social_currency) {
        if (id_social_currency.isEmpty())
            throw new InvalidDriverSocialSecurityNumberException("SOCIAL SECURITY NUMBER IS NULL");
    }
}

package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CreateNewDrivingLicence {

    private final InMemoryDatabase database;

    private final DrivingLicenceIdGenerationService DrivingLicenceId;


    public Optional<DrivingLicence> CreateNewDrivingLicence(String id_social_currency) {
        Optional<DrivingLicence> result;
        if (id_social_currency.isEmpty()) {
            throw new InvalidDriverSocialSecurityNumberException("SOCIAL SECURITY NUMBER IS NULL");
        }
        if (id_social_currency.length() == 14) {
            throw new InvalidDriverSocialSecurityNumberException("SOCIAL SECURITY NUMBER HAVE NOT 15 CHAR");
        }
        try {
            Float f = Float.parseFloat(id_social_currency);
        }
        catch (NumberFormatException e){
            throw new InvalidDriverSocialSecurityNumberException("SOCIAL SECURITY NUMBER IS NOT NUMBER");
        }
        DrivingLicence drivingLicence = DrivingLicence.builder()
                .driverSocialSecurityNumber(id_social_currency)
                .build();
        result = Optional.ofNullable(database.save(DrivingLicenceId.generateNewDrivingLicenceId(), drivingLicence));
        return result;
    }
}

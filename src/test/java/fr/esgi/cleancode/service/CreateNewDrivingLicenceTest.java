package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DrivingLicenceCreateServiceTest {

    private final InMemoryDatabase db = InMemoryDatabase.getInstance();
    private final CreateNewDrivingLicenceService service = new CreateNewDrivingLicenceService(db, new DrivingLicenceIdGenerationService());

    private final DrivingLicenceFinderService find = new DrivingLicenceFinderService(db);

    @Test
    void should_not_create_null_ID_social_security() {
        Exception exception = assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            final var actual = service.CreateNewDrivingLicence ("");
        });

        String expectedMessage = "SOCIAL SECURITY NUMBER IS NULL";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void should_not_create_invalide_ID_social_security() {
        Exception exception = assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            final var actual = service.CreateNewDrivingLicence("1");
        });

        String expectedMessage = "SOCIAL SECURITY NUMBER HAVE NOT 15 CHAR";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }


    @Test
    void should_not_create_not_number_ID_social_security() {
        Exception exception = assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            final var actual = service.CreateNewDrivingLicence("aaaaaaaaaaaaaaa");
        });

        String expectedMessage = "SOCIAL SECURITY NUMBER IS NOT NUMBER";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void should_create_with_12_point() {
        final var actual = service.CreateNewDrivingLicence("111111111111111");

        assertTrue(actual.get().getAvailablePoints() == 12);
    }

    @Test
    void should_create_with_correct_security_social() {
        final var actual = service.CreateNewDrivingLicence("111111111111111");

        assertTrue(!actual.get().getDriverSocialSecurityNumber().isEmpty());
    }

    @Test
    void should_create_correct_save_driving_licence() {
        final var res = service.CreateNewDrivingLicence("111111111111111");
        final var actual = find.findById(res.get().getId());

        assertTrue(!actual.isEmpty()) ;
    }
}
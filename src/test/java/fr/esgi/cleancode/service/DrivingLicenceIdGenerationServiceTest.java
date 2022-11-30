package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.InvalidDriverSocialSecurityNumberException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DrivingLicenceIdGenerationServiceTest {
    private final DrivingLicenceIdGenerationService service = new DrivingLicenceIdGenerationService();

    private final InMemoryDatabase db = InMemoryDatabase.getInstance();

    private final DrivingLicenceFinderService find = new DrivingLicenceFinderService(db);
    @Test
    void should_generate_valid_UUID() {
        final var actual = service.generateNewDrivingLicenceId();
        assertThat(actual)
                .isNotNull()
                .isEqualTo(UUID.fromString(actual.toString()));
    }

    @Test
    void should_not_create_null_ID_social_security() {
        Exception exception = assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            final var actual = service.generateNewDrivingLicenceId(null);
        });

        String expectedMessage = "SOCIAL SECURITY NUMBER IS NULL";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void should_not_create_invalide_ID_social_security() {
        Exception exception = assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            final var actual = service.generateNewDrivingLicenceId("1");
        });

        String expectedMessage = "SOCIAL SECURITY NUMBER HAVE NOT 15 CHAR";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void should_not_create_not_number_ID_social_security() {
        Exception exception = assertThrows(InvalidDriverSocialSecurityNumberException.class, () -> {
            final var actual = service.generateNewDrivingLicenceId("a");
        });

        String expectedMessage = "SOCIAL SECURITY NUMBER IS NOT NUMBER";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void should_create_with_12_point() {
        final var actual = service.generateNewDrivingLicenceId("111111111111111");

        assertTrue(actual.get().getAvailablePoints() == 12);
    }

    @Test
    void should_create_with_correct_security_social() {
        final var actual = service.generateNewDrivingLicenceId("111111111111111");

        assertTrue(!actual.get().getDriverSocialSecurityNumber().isEmpty());
    }

    @Test
    void should_create_correct_save_driving_licence() {
        final var res = service.generateNewDrivingLicenceId("111111111111111");
        final var actual = find.findById(res.get().getId());

        assertTrue(actual.isEmpty()) ;
    }
}
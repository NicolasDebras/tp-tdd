package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DrivingLicenceFinderServiceTest {

    @InjectMocks
    private DrivingLicenceFinderService service;

    @Mock
    private InMemoryDatabase database;

    DrivingLicence fakeDrivingLicence;

    @BeforeEach
    void setUpDatabase() {
        CreateNewDrivingLicence creationService = new CreateNewDrivingLicence(database, new DrivingLicenceIdGenerationService());

        this.fakeDrivingLicence = creationService.CreateNewDrivingLicence("000000000000000").get();
    }

    @Test
    @DisplayName("Should find driving licence with id")
    void should_find() {
        var foundDrivingLicence = service.findById(fakeDrivingLicence.getId());

        Assertions.assertTrue(foundDrivingLicence.isPresent());
    }

    @Test
    @DisplayName("Should not find driving licence with id")
    void should_not_find() {
        var foundDrivingLicence = service.findById(UUID.randomUUID());

        Assertions.assertTrue(foundDrivingLicence.isEmpty());
    }
}
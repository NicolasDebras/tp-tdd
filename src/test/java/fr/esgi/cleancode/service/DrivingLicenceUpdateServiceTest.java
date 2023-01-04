package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DrivingLicenceUpdateServiceTest {

    @InjectMocks
    private DrivingLicenceUpdateService service;

    @Mock
    private InMemoryDatabase database ;
    private DrivingLicence drivingLicence ;
    private final UUID drivingLicenceId = DrivingLicenceIdGenerationService.generateNewDrivingLicenceId();

    @BeforeEach
    public void init_Valid_Driving_Licence () {
        drivingLicence = DrivingLicence.builder()
                .id(drivingLicenceId)
                .driverSocialSecurityNumber("123456789012345")
                .build();
    }

    @Test
    @DisplayName("should throw ResourceNotFoundException if driving licence not found")
    public void should_throw_if_driving_licence_not_found () {
        when(database.findById(drivingLicenceId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.RemovePoints(drivingLicenceId, 6);
        });
    }

    @Test
    @DisplayName("should not remove points if driving licence have 0 point")
    public void should_Not_Remove_Points_Below_Zero () {
        DrivingLicence drivingLicenceWithZeroPoints = drivingLicence.withAvailablePoints(0);

        when(database.findById(drivingLicenceId)).thenReturn(Optional.ofNullable(drivingLicence));
        when(database.save(drivingLicenceId, drivingLicenceWithZeroPoints)).thenReturn(drivingLicenceWithZeroPoints);

        final DrivingLicence updatedDrivingLicence = service.RemovePoints(drivingLicenceId, 13);
        Assertions.assertEquals(0, updatedDrivingLicence.getAvailablePoints());
    }

    @Test
    @DisplayName("should remove 6 points")
    public void should_Remove_6_points () {
        DrivingLicence drivingLicenceWithSixPoints = drivingLicence.withAvailablePoints(6);

        when(database.findById(drivingLicenceId)).thenReturn(Optional.ofNullable(drivingLicence));
        when(database.save(drivingLicenceId, drivingLicenceWithSixPoints)).thenReturn(drivingLicenceWithSixPoints);

        final DrivingLicence updatedDrivingLicence = service.RemovePoints(drivingLicenceId, 6) ;
        Assertions.assertEquals(drivingLicenceWithSixPoints.getAvailablePoints(), updatedDrivingLicence.getAvailablePoints());
    }



}

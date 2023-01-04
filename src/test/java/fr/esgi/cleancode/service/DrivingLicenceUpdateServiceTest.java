package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private DrivingLicence validDrivingLicence ;
    private final UUID drivingLicenceId = DrivingLicenceIdGenerationService.generateNewDrivingLicenceId();

    @BeforeEach
    public void init_Valid_Driving_Licence () {
        validDrivingLicence = DrivingLicence.builder()
                .id(drivingLicenceId)
                .driverSocialSecurityNumber("123456789012345")
                .build();
    }

    @Test
    public void should_throw_ff_driving_licence_not_found () {
        when(database.findById(drivingLicenceId)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.RemovePoints(drivingLicenceId, 6);
        });
    }

    @Test
    public void should_Not_Remove_Points_Below_Zero () {
        DrivingLicence drivingLicenceWithZeroPoints = validDrivingLicence.withAvailablePoints(0);

        when(database.findById(drivingLicenceId)).thenReturn(Optional.ofNullable(validDrivingLicence));
        when(database.save(drivingLicenceId, drivingLicenceWithZeroPoints)).thenReturn(drivingLicenceWithZeroPoints);

        Assertions.assertEquals(12, validDrivingLicence.getAvailablePoints());

        final DrivingLicence updatedDrivingLicence = service.RemovePoints(drivingLicenceId, 13);
        Assertions.assertEquals(0, updatedDrivingLicence.getAvailablePoints());
    }

    @Test
    public void should_Update_Given_Number_Of_Points () {
        DrivingLicence drivingLicenceWithSixPoints = validDrivingLicence.withAvailablePoints(6);

        when(database.findById(drivingLicenceId)).thenReturn(Optional.ofNullable(validDrivingLicence));
        when(database.save(drivingLicenceId, drivingLicenceWithSixPoints)).thenReturn(drivingLicenceWithSixPoints);

        Assertions.assertEquals(12, validDrivingLicence.getAvailablePoints());
        final DrivingLicence updatedDrivingLicence = service.RemovePoints(drivingLicenceId, 6) ;
        Assertions.assertEquals(drivingLicenceWithSixPoints.getAvailablePoints(), updatedDrivingLicence.getAvailablePoints());
    }

    @Test
    public void should_Update_And_Return_Driving_Licence () {
        DrivingLicence drivingLicenceWithSixPoints = validDrivingLicence.withAvailablePoints(6);

        when(database.findById(drivingLicenceId)).thenReturn(Optional.ofNullable(validDrivingLicence));
        when(database.save(drivingLicenceId, drivingLicenceWithSixPoints)).thenReturn(drivingLicenceWithSixPoints);

        final DrivingLicence updatedDrivingLicence = service.RemovePoints(drivingLicenceId, 6) ;
        Assertions.assertEquals(drivingLicenceWithSixPoints, updatedDrivingLicence);
    }

}

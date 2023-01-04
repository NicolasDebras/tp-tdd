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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DrivingLicenceFinderServiceTest {

    @InjectMocks
    private DrivingLicenceFinderService service;

    @Mock
    private InMemoryDatabase database;

    DrivingLicence fakeDrivingLicence;


    @Test
    @DisplayName("Should find driving licence with id")
    void should_find() {
        final var id  = UUID.randomUUID();
        final var drivingLicence  = DrivingLicence.builder().id(id).build();

        when(database.findById(id)).thenReturn(Optional.of(drivingLicence));

        final var actual  = service.findById(id);

        assertThat(actual).containsSame(drivingLicence);
        verify(database).findById(id);
        //verifyNoMoreInteraction(database);
    }

    @Test
    @DisplayName("Should not find driving licence with id")
    void should_not_find() {
        var foundDrivingLicence = service.findById(UUID.randomUUID());

        Assertions.assertTrue(foundDrivingLicence.isEmpty());
    }
}
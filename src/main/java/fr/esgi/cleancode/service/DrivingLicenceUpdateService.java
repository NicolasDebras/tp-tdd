package fr.esgi.cleancode.service;

import fr.esgi.cleancode.database.InMemoryDatabase;
import fr.esgi.cleancode.exception.ResourceNotFoundException;
import fr.esgi.cleancode.model.DrivingLicence;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DrivingLicenceUpdateService {

    private final InMemoryDatabase database;

    public DrivingLicence RemovePoints(UUID drivingLicenceId, Integer pointsTORemove)  throws ResourceNotFoundException {
        // Find the driving license to update in the database
        var drivingLicenceToUpdate = this.database.findById(drivingLicenceId) ;
        // If the driving license is not found, throw a ResourceNotFoundException
        if (drivingLicenceToUpdate.isEmpty()) {
            throw new ResourceNotFoundException("Driving licence with id " + drivingLicenceId.toString() + " not found");
        }
        // Get the updated driving license object
        DrivingLicence updatedDrivingLicence = drivingLicenceToUpdate.get();
        // Calculate the updated number of points, making sure it doesn't go below zero
        final var updatedPoints = Math.max(updatedDrivingLicence.getAvailablePoints() - pointsTORemove, 0);
        // Update the driving license object with the new number of points
        updatedDrivingLicence = updatedDrivingLicence.withAvailablePoints(updatedPoints);
        // Save the updated driving license object to the database and return it
        return database.save(drivingLicenceId, updatedDrivingLicence);
    }

}

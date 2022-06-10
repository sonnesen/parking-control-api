package com.parkingcontrol.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParkingSpotAlreadyInUseException extends Exception {
    public ParkingSpotAlreadyInUseException(String parkingSpotNumber) {
        super(String.format("Parking Spot %s is already in use.", parkingSpotNumber));
    }
}

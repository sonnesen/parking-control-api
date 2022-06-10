package com.parkingcontrol.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParkingSpotAlreadyRegistered extends Exception {
    public ParkingSpotAlreadyRegistered(String apartment, String block) {
        super(String.format("Parking Spot already registered for apartment %s and block %s.", apartment, block));
    }
}

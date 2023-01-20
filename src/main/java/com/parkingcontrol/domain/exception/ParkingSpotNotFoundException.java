package com.parkingcontrol.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParkingSpotNotFoundException extends RuntimeException {
    public ParkingSpotNotFoundException(UUID uuid) {
        super(String.format("Parking Spot with id %s not found.", uuid));
    }
}

package com.parkingcontrol.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParkingSpotLicensePlateCarAlreadyInUseException extends RuntimeException {
    public ParkingSpotLicensePlateCarAlreadyInUseException(String licensePlateCar) {
        super(String.format("License Plate Car %s is already in use.", licensePlateCar));
    }
}

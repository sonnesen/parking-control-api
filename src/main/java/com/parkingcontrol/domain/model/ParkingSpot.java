package com.parkingcontrol.domain.model;

import com.parkingcontrol.api.dto.ParkingSpotDto;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_PARKING_SPOT")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 10)
    private String parkingSpotNumber;

    @Embedded
    @Column(nullable = false, unique = true)
    private Vehicle vehicle = new Vehicle();

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false, length = 130)
    private String responsibleName;

    @Column(nullable = false, length = 30)
    private String apartment;

    @Column(nullable = false, length = 30)
    private String block;

    public void setVehicleFromDto(ParkingSpotDto dto) {
        this.vehicle = new Vehicle(dto.getLicensePlateCar(),
                dto.getBrandCar(),
                dto.getModelCar(),
                dto.getColorCar());
    }
}

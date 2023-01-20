package com.parkingcontrol.domain.service;

import com.parkingcontrol.api.request.CreateParkingSpotRequest;
import com.parkingcontrol.api.request.UpdateParkingSpotRequest;
import com.parkingcontrol.api.response.ParkingSpotResponse;
import com.parkingcontrol.domain.exception.ParkingSpotAlreadyInUseException;
import com.parkingcontrol.domain.exception.ParkingSpotAlreadyRegistered;
import com.parkingcontrol.domain.exception.ParkingSpotLicensePlateCarAlreadyInUseException;
import com.parkingcontrol.domain.exception.ParkingSpotNotFoundException;
import com.parkingcontrol.domain.model.ParkingSpot;
import com.parkingcontrol.domain.model.Vehicle;
import com.parkingcontrol.domain.repository.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ParkingSpotResponse create(CreateParkingSpotRequest request) {
        doVerifications(request);
        ParkingSpot parkingSpot = toModel(request);
        parkingSpot.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        ParkingSpot createdParkingSpot = parkingSpotRepository.save(parkingSpot);
        return toResponse(createdParkingSpot);
    }

    public ParkingSpotResponse updateParkingSpot(UUID id, UpdateParkingSpotRequest createParkingSpotRequest) {
        ParkingSpot existentParkingSpot = verifyIfExists(id);
        ParkingSpot parkingSpotToUpdate = toModel(createParkingSpotRequest);
        parkingSpotToUpdate.setId(existentParkingSpot.getId());
        parkingSpotToUpdate.getVehicle().setLicensePlateCar(existentParkingSpot.getVehicle().getLicensePlateCar());
        parkingSpotToUpdate.setRegistrationDate(existentParkingSpot.getRegistrationDate());
        parkingSpotToUpdate.setParkingSpotNumber(existentParkingSpot.getParkingSpotNumber());
        parkingSpotToUpdate.setApartment(existentParkingSpot.getApartment());
        parkingSpotToUpdate.setBlock(existentParkingSpot.getBlock());
        parkingSpotToUpdate.setParkingSpotNumber(existentParkingSpot.getParkingSpotNumber());
        ParkingSpot updatedParkingSpot = parkingSpotRepository.save(parkingSpotToUpdate);
        return toResponse(updatedParkingSpot);
    }

    public Page<ParkingSpotResponse> findAll(Pageable pageable) {
        return new PageImpl<>(parkingSpotRepository.findAll(pageable).stream().map(this::toResponse).toList());
    }

    public ParkingSpotResponse findById(UUID id) {
        ParkingSpot foundParkingSpot = parkingSpotRepository.findById(id).orElseThrow(() -> new ParkingSpotNotFoundException(id));
        return toResponse(foundParkingSpot);
    }

    @Transactional
    public void deleteById(UUID id) {
        parkingSpotRepository.deleteById(id);
    }

    private void verifyIfExistsByLicensePlateCar(String licensePlateCar) {
        if (parkingSpotRepository.existsByVehicleLicensePlateCar(licensePlateCar)) {
            throw new ParkingSpotLicensePlateCarAlreadyInUseException(licensePlateCar);
        }
    }

    private void verifyIfExistsByParkingSpotNumber(String parkingSpotNumber) {
        if (parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber)) {
            throw new ParkingSpotAlreadyInUseException(parkingSpotNumber);
        }
    }

    private void verifyIfExistsByApartmentAndBlock(String apartment, String block) {
        if (parkingSpotRepository.existsByApartmentAndBlock(apartment, block)) {
            throw new ParkingSpotAlreadyRegistered(apartment, block);
        }
    }

    private ParkingSpot verifyIfExists(UUID id) {
        return parkingSpotRepository.findById(id).orElseThrow(() -> new ParkingSpotNotFoundException(id));
    }

    private void doVerifications(CreateParkingSpotRequest request) {
        verifyIfExistsByLicensePlateCar(request.getLicensePlateCar());
        verifyIfExistsByParkingSpotNumber(request.getParkingSpotNumber());
        verifyIfExistsByApartmentAndBlock(request.getApartment(), request.getBlock());
    }

    private ParkingSpot toModel(CreateParkingSpotRequest request) {
        ParkingSpot model = modelMapper.map(request, ParkingSpot.class);
        Vehicle vehicle = Vehicle.builder()
                .modelCar(request.getModelCar())
                .colorCar(request.getColorCar())
                .brandCar(request.getBrandCar())
                .licensePlateCar(request.getLicensePlateCar())
                .build();
        model.setVehicle(vehicle);
        return model;
    }

    private ParkingSpot toModel(UpdateParkingSpotRequest request) {
        ParkingSpot model = modelMapper.map(request, ParkingSpot.class);
        Vehicle vehicle = Vehicle.builder()
                .modelCar(request.getModelCar())
                .colorCar(request.getColorCar())
                .brandCar(request.getBrandCar())
                .build();
        model.setVehicle(vehicle);
        return model;
    }

    private ParkingSpotResponse toResponse(ParkingSpot model) {
        ParkingSpotResponse response = modelMapper.map(model, ParkingSpotResponse.class);
        response.setLicensePlateCar(model.getVehicle().getLicensePlateCar());
        response.setColorCar(model.getVehicle().getColorCar());
        response.setBrandCar(model.getVehicle().getBrandCar());
        response.setModelCar(model.getVehicle().getModelCar());
        return response;
    }
}

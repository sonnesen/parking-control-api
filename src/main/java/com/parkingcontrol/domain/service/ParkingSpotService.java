package com.parkingcontrol.domain.service;

import com.parkingcontrol.api.dto.ParkingSpotDto;
import com.parkingcontrol.domain.exception.ParkingSpotAlreadyInUseException;
import com.parkingcontrol.domain.exception.ParkingSpotAlreadyRegistered;
import com.parkingcontrol.domain.exception.ParkingSpotLicensePlateCarAlreadyInUseException;
import com.parkingcontrol.domain.exception.ParkingSpotNotFoundException;
import com.parkingcontrol.domain.model.ParkingSpot;
import com.parkingcontrol.domain.model.Vehicle;
import com.parkingcontrol.domain.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ParkingSpotDto save(ParkingSpotDto parkingSpotDto) throws Exception {
        doVerifications(parkingSpotDto);
        ParkingSpot parkingSpot = toModel(parkingSpotDto);
        parkingSpot.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        ParkingSpot savedParkingSpot = parkingSpotRepository.save(parkingSpot);
        return toDto(savedParkingSpot);
    }

    public ParkingSpotDto updateParkingSpot(UUID id, ParkingSpotDto parkingSpotDto) throws Exception {
        doVerifications(parkingSpotDto);
        ParkingSpot existentParkingSpot = verifyIfExists(id);
        ParkingSpot parkingSpotToUpdate = toModel(parkingSpotDto);
        parkingSpotToUpdate.setId(existentParkingSpot.getId());
        parkingSpotToUpdate.setRegistrationDate(existentParkingSpot.getRegistrationDate());
        ParkingSpot updatedParkingSpot = parkingSpotRepository.save(parkingSpotToUpdate);
        return toDto(updatedParkingSpot);
    }

    private void verifyIfExistsByLicensePlateCar(String licensePlateCar) throws ParkingSpotLicensePlateCarAlreadyInUseException {
        if (parkingSpotRepository.existsByVehicleLicensePlateCar(licensePlateCar)) {
            throw new ParkingSpotLicensePlateCarAlreadyInUseException(licensePlateCar);
        }
    }

    private void verifyIfExistsByParkingSpotNumber(String parkingSpotNumber) throws ParkingSpotAlreadyInUseException {
        if (parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber)) {
            throw new ParkingSpotAlreadyInUseException(parkingSpotNumber);
        }
    }

    private void verifyIfExistsByApartmentAndBlock(String apartment, String block) throws ParkingSpotAlreadyRegistered {
        if (parkingSpotRepository.existsByApartmentAndBlock(apartment, block)) {
            throw new ParkingSpotAlreadyRegistered(apartment, block);
        }
    }

    public Page<ParkingSpotDto> findAll(Pageable pageable) {
        return new PageImpl<>(parkingSpotRepository.findAll(pageable).stream().map(this::toDto).collect(Collectors.toList()));
    }

    public ParkingSpotDto findById(UUID id) throws ParkingSpotNotFoundException {
        ParkingSpot foundParkingSpot = parkingSpotRepository.findById(id).orElseThrow(() -> new ParkingSpotNotFoundException(id));
        return toDto(foundParkingSpot);
    }

    @Transactional
    public void deleteById(UUID id) {
        parkingSpotRepository.deleteById(id);
    }

    private ParkingSpot verifyIfExists(UUID id) throws ParkingSpotNotFoundException {
        return parkingSpotRepository.findById(id).orElseThrow(() -> new ParkingSpotNotFoundException(id));
    }

    private void doVerifications(ParkingSpotDto parkingSpotDto) throws Exception {
        verifyIfExistsByLicensePlateCar(parkingSpotDto.getLicensePlateCar());
        verifyIfExistsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
        verifyIfExistsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock());
    }

    private ParkingSpot toModel(ParkingSpotDto dto) {
        ParkingSpot model = modelMapper.map(dto, ParkingSpot.class);
        Vehicle vehicle = Vehicle.builder()
                .modelCar(dto.getModelCar())
                .colorCar(dto.getColorCar())
                .brandCar(dto.getBrandCar())
                .licensePlateCar(dto.getLicensePlateCar())
                .build();
        model.setVehicle(vehicle);
        return model;
    }

    private ParkingSpotDto toDto(ParkingSpot model) {
        ParkingSpotDto dto = modelMapper.map(model, ParkingSpotDto.class);
        dto.setLicensePlateCar(model.getVehicle().getLicensePlateCar());
        dto.setColorCar(model.getVehicle().getColorCar());
        dto.setBrandCar(model.getVehicle().getBrandCar());
        dto.setModelCar(model.getVehicle().getModelCar());
        return dto;
    }
}

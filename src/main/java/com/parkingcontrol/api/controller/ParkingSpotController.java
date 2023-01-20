package com.parkingcontrol.api.controller;

import com.parkingcontrol.api.dto.ParkingSpotDto;
import com.parkingcontrol.domain.exception.ParkingSpotNotFoundException;
import com.parkingcontrol.domain.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @PostMapping
    public ResponseEntity<ParkingSpotDto> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotDto));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotDto>> getAllParkingSpots(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpotDto> getOneParkingSpot(@PathVariable(value = "id") UUID id) throws ParkingSpotNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
        parkingSpotService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpotDto> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                            @RequestBody @Valid ParkingSpotDto parkingSpotDto)
            throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.updateParkingSpot(id, parkingSpotDto));
    }

}

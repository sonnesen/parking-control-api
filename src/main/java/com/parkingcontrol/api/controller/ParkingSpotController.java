package com.parkingcontrol.api.controller;

import com.parkingcontrol.api.request.CreateParkingSpotRequest;
import com.parkingcontrol.api.request.UpdateParkingSpotRequest;
import com.parkingcontrol.api.response.ParkingSpotResponse;
import com.parkingcontrol.domain.service.ParkingSpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @PostMapping
    public ResponseEntity<ParkingSpotResponse> create(@RequestBody @Valid CreateParkingSpotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotResponse>> getAllParkingSpots(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpotResponse> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
        parkingSpotService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpotResponse> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                                 @RequestBody @Valid UpdateParkingSpotRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.updateParkingSpot(id, request));
    }

}

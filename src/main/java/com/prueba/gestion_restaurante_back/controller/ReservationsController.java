package com.prueba.gestion_restaurante_back.controller;

import com.prueba.gestion_restaurante_back.dto.ReservationDTO;
import com.prueba.gestion_restaurante_back.dto.TableDTO;
import com.prueba.gestion_restaurante_back.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationsController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ReservationDTO>> getActiveReservations() {
        return ResponseEntity.ok(reservationService.getActiveReservations());
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        return ResponseEntity.ok(reservationService.createReservation(reservationDTO));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ReservationDTO> confirmReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirmReservation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationDTO> deleteReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}

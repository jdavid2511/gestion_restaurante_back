package com.prueba.gestion_restaurante_back.dto;

import com.prueba.gestion_restaurante_back.model.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long id;
    private CustomerDTO customerDTO;
    private Long tableId;
    private LocalDateTime reservationDate;
    private Integer numberOfPeople;
    private Double durationHours;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
}

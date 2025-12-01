package com.prueba.gestion_restaurante_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantConfigDTO {
    private Long id;
    private LocalTime close_time;
    private LocalTime open_time;
    private Double max_waiting_time;
}

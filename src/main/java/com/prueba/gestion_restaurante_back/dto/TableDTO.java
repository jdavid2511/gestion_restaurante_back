package com.prueba.gestion_restaurante_back.dto;

import com.prueba.gestion_restaurante_back.model.table.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO {
    private Long id;
    private Integer tableNumber;
    private Integer capacity;
    private TableStatus status;
    private Boolean isTableVip;
}

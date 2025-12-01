package com.prueba.gestion_restaurante_back.dto;

import com.prueba.gestion_restaurante_back.model.customer.RangeLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private Boolean isCustomerVip;
    private RangeLevel rangeLevel;
    private Integer points;
}

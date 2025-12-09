package com.prueba.gestion_restaurante_back.model.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nit;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Boolean isCustomerVip = false;

    private int points = 0;

    private RangeLevel rangeLevel = RangeLevel.BRONCE;

    public void addPoints(Integer points) {
        this.points += points;
        updateRangeLevel();
    }

    public void updateRangeLevel() {
        if (points >= 100) {
            this.rangeLevel = RangeLevel.ORO;
        } else if (points >= 50) {
            this.rangeLevel = RangeLevel.PLATA;
        } else {
            this.rangeLevel = RangeLevel.BRONCE;
        }
    }
}



package com.prueba.gestion_restaurante_back.model.waitlist;

import com.prueba.gestion_restaurante_back.model.customer.Customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "waitlist")
@NoArgsConstructor
@AllArgsConstructor
public class WaitList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    @Column(nullable = false)
    private Integer numberOfPeople;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}

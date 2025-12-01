package com.prueba.gestion_restaurante_back.repository;

import com.prueba.gestion_restaurante_back.model.customer.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPhone(String phone);
    //List<Customer> findByCustomerVip();
}

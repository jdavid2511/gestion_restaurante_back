package com.prueba.gestion_restaurante_back.service;

import com.prueba.gestion_restaurante_back.dto.CustomerDTO;
import com.prueba.gestion_restaurante_back.model.customer.Customer;
import com.prueba.gestion_restaurante_back.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer findOrCreateCustomer(CustomerDTO customerDTO) {
        Optional<Customer> existing = customerRepository.findByPhone(customerDTO.getPhone());

        if (existing.isPresent()) {
            return existing.get();
        }

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setPhone(customerDTO.getPhone());
        customer.setEmail(customerDTO.getEmail());
        customer.setIsCustomerVip(customerDTO.getIsCustomerVip() != null ? customerDTO.getIsCustomerVip() : false);

        return customerRepository.save(customer);
    }

    public CustomerDTO covertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getIsCustomerVip(),
                customer.getRangeLevel(),
                customer.getPoints()
        );
    }
}

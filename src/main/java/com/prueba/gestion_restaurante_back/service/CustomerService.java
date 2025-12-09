package com.prueba.gestion_restaurante_back.service;

import com.prueba.gestion_restaurante_back.dto.CustomerDTO;
import com.prueba.gestion_restaurante_back.model.customer.Customer;
import com.prueba.gestion_restaurante_back.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByNit(customerDTO.getNit())) {
            throw new RuntimeException("Ya el cliente se encuentra registrado");
        }

        Customer customer = convertToEntity(customerDTO);
        Customer saved = customerRepository.save(customer);
        return convertToDTO(saved);
    }

    @Transactional
    public CustomerDTO updateCustomer(CustomerDTO customerDTO, Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("no existe mesa"));

        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setNit(customerDTO.getNit());
        customer.setEmail(customerDTO.getEmail());
        customer.setIsCustomerVip(customerDTO.getIsCustomerVip());
        customer.addPoints(customerDTO.getPoints());
        customer.setRangeLevel(customerDTO.getRangeLevel());

        Customer save = customerRepository.save(customer);
        return convertToDTO(save);
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().
                map(this::convertToDTO).collect(Collectors.toList());
    }

    public CustomerDTO getCustomerByNit(String nit) {
        Customer customer = customerRepository.findByNit(nit).orElseThrow(() -> new RuntimeException("No existe un cliente con ese NIT"));
        return convertToDTO(customer);
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe un cliente con ese NIT"));
        return convertToDTO(customer);
    }

    public Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setNit(customerDTO.getNit());
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setRangeLevel(customerDTO.getRangeLevel());
        customer.setPoints(customerDTO.getPoints());
        customer.setPoints(customerDTO.getPoints());

        return customer;
    }

    public CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getNit(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getIsCustomerVip(),
                customer.getRangeLevel(),
                customer.getPoints()
        );
    }
}

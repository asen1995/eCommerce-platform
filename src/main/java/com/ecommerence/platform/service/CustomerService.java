package com.ecommerence.platform.service;

import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.entity.Customer;
import com.ecommerence.platform.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDto register(CustomerDto customerDto) {

        Customer customer = new Customer();
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setCity(customerDto.getCity());
        customer.setCreatedDate(new Date());

        customerRepository.save(customer);

        return customerDto;
    }
}

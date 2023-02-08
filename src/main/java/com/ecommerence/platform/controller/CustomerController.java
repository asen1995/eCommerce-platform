package com.ecommerence.platform.controller;

import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.entity.Customer;
import com.ecommerence.platform.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/register")
    public ResponseEntity<CustomerDto> register(@RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.register(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

}

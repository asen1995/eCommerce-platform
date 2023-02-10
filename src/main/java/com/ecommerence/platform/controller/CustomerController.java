package com.ecommerence.platform.controller;

import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.service.CustomerService;
import com.ecommerence.platform.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final OrderService orderService;

    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }


    @PostMapping("/register")
    public ResponseEntity<CustomerDto> register(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.register(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }


    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> orderGlobalSearch(@RequestParam(value = "search") String search) {

        List<OrderDto> orderDtoList = orderService.orderSearchForLoggedUser(search);

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }


}

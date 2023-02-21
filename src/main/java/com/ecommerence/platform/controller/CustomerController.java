package com.ecommerence.platform.controller;

import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.service.ICustomerService;
import com.ecommerence.platform.service.IOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final ICustomerService customerService;

    private final IOrderService orderService;

    public CustomerController(ICustomerService customerService, IOrderService orderService) {
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
    @GetMapping
    public ResponseEntity<List<CustomerDto>> searchCustomers(@RequestParam(value = "search") String search,
                                                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {


        List<CustomerDto> customersResponse = customerService.searchCustomers(search, page, pageSize);

        return new ResponseEntity<>(customersResponse, HttpStatus.OK);
    }


}

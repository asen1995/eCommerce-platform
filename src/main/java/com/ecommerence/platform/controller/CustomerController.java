package com.ecommerence.platform.controller;

import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.service.ICustomerService;
import com.ecommerence.platform.service.IOrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private static final Logger logger = LogManager.getLogger(CustomerController.class);

    private final ICustomerService customerService;

    private final IOrderService orderService;

    public CustomerController(ICustomerService customerService, IOrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }


    @PostMapping("/register")
    public ResponseEntity<CustomerDto> register(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.register(customerDto);

        logger.info("User {} registered successfully", savedCustomer.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }


    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> orderGlobalSearch(@RequestParam(value = "search") String search) {

        logger.info("Searching for orders with search: {}", search);
        List<OrderDto> orderDtoList = orderService.orderSearchForLoggedUser(search);

        logger.info("OrderGlobalSearch finished successfully");
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CUSTOMER_MANAGER')")
    public ResponseEntity<List<CustomerDto>> searchCustomers(@RequestParam(value = "search") String search,
                                                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        logger.info("Searching for customers with search: {} and page: {} and pageSize: {}", search, page, pageSize);
        List<CustomerDto> customersResponse = customerService.searchCustomers(search, page, pageSize);

        logger.info("SearchCustomers finished successfully");
        return new ResponseEntity<>(customersResponse, HttpStatus.OK);
    }


}

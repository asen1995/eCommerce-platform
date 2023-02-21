package com.ecommerence.platform.service;

import com.ecommerence.platform.dto.CustomerDto;

import java.util.List;

public interface ICustomerService {
    CustomerDto register(CustomerDto customerDto);

    List<CustomerDto> searchCustomers(String search, Integer page, Integer pageSize);
}

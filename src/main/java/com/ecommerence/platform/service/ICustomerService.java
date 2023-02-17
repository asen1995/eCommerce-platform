package com.ecommerence.platform.service;

import com.ecommerence.platform.dto.CustomerDto;

public interface ICustomerService {
    CustomerDto register(CustomerDto customerDto);
}

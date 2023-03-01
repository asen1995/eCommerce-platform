package com.ecommerence.platform.mapper;

import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.entity.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDto toCustomerDto(Customer customer);

    Customer toCustomerEntity(CustomerDto customerDto);
}

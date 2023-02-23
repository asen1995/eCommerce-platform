package com.ecommerence.platform.mapper;

import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.entity.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {
    OrderDto toOrderDto(Order order);
}

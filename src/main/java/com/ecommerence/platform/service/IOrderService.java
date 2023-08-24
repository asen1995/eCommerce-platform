package com.ecommerence.platform.service;

import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.exception.CustomerNotFoundException;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.exception.ProductQuantityNotEnoughException;
import com.ecommerence.platform.response.OrderResponse;

import java.util.List;

public interface IOrderService {

    OrderResponse orderProduct(Integer id, Integer orderedQuantity) throws Exception;

    OrderDto createOrder(OrderDto orderDto) throws CustomerNotFoundException, ProductNotFoundException, ProductQuantityNotEnoughException;

    List<OrderDto> orderGlobalSearch(String search);

    List<OrderDto> orderSearchForLoggedUser(String search);

    OrderDto approveOrder(Integer id) throws Exception;
}

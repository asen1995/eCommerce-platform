package com.ecommerence.platform.controller;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.response.OrderResponse;
import com.ecommerence.platform.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/v1/products/{id}/order/{quantity}")
    public ResponseEntity<OrderResponse> orderProduct(@PathVariable("id") Integer id, @PathVariable("quantity") Integer quantity) throws Exception {

        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OrderResponse(AppConstants.QTY_MUST_BE_GREATER_THAN_ZERO_MESSAGE, null));
        }

        OrderResponse orderResponse = orderService.orderProduct(id, quantity);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderResponse);
    }

    @PostMapping("/v1/products/order")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderDto order) throws Exception {

        OrderResponse orderResponse = orderService.placeOrder(order);


        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

}

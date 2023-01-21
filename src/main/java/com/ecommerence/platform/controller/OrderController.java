package com.ecommerence.platform.controller;

import com.ecommerence.platform.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/product/{id}/order/{quantity}")
    public ResponseEntity<String> orderProduct(@PathVariable("id") Integer id, @PathVariable("quantity") Integer quantity) throws Exception {

        if(quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Quantity must be greater than 0");
        }

        String orderMessage = orderService.orderProduct(id, quantity);

        return ResponseEntity.status(HttpStatus.OK)
                .body(String.format(orderMessage));
    }
}

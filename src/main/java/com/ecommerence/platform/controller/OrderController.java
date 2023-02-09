package com.ecommerence.platform.controller;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.exception.OrderNotFoundException;
import com.ecommerence.platform.response.OrderResponse;
import com.ecommerence.platform.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/{productId}/order/{quantity}")
    public ResponseEntity<OrderResponse> orderProduct(@PathVariable("productId") Integer productId, @PathVariable("quantity") Integer quantity) throws Exception {

        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OrderResponse(AppConstants.QTY_MUST_BE_GREATER_THAN_ZERO_MESSAGE, null));
        }

        OrderResponse orderResponse = orderService.orderProduct(productId, quantity);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderResponse);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto order) throws Exception {

        OrderDto orderDto = orderService.createOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderDto);
    }


    @GetMapping
    public ResponseEntity<List<OrderDto>> orderGlobalSearch(@RequestParam(value = "search") String search) {

        List<OrderDto> orderDtoList = orderService.orderGlobalSearch(search);

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<OrderDto> approve(@PathVariable("id") Integer id) throws OrderNotFoundException {

        OrderDto orderDto = orderService.approveOrder(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(orderDto);
    }
}

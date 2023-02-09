package com.ecommerence.platform.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private String name;
    private String comment;
    private List<Integer> productIds;
    private Integer customerId;

}

package com.ecommerence.platform.dto;

import lombok.Data;

@Data
public class CustomerDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
}

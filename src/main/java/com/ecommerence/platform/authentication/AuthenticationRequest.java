package com.ecommerence.platform.authentication;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String username;
    private String password;
}

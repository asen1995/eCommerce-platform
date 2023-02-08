package com.ecommerence.platform.controller;

import com.ecommerence.platform.entity.Customer;
import com.ecommerence.platform.security.JwtTokenProvider;
import com.ecommerence.platform.service.CustomerService;
import com.ecommerence.platform.service.CustomerUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    private final CustomerUserDetailsService customerUserDetailsService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    public CustomerController(CustomerService customerService,
                              CustomerUserDetailsService customerUserDetailsService,
                              AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.customerService = customerService;
        this.customerUserDetailsService = customerUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> register(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.register(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Customer loginRequest) throws Exception {
        System.out.println("loginRequest = " + loginRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtTokenProvider.generateToken(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}

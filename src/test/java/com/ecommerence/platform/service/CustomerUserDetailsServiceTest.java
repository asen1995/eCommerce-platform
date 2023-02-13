package com.ecommerence.platform.service;

import com.ecommerence.platform.entity.Customer;
import com.ecommerence.platform.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerUserDetailsService.class)
@ActiveProfiles("test")
public class CustomerUserDetailsServiceTest {


    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;
    @MockBean
    private CustomerRepository customerRepository;


    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameNotFoundUser() throws Exception {

        when(customerRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        customerUserDetailsService.loadUserByUsername("janKowalski");

    }

    @Test
    public void testLoadUserByUsernameForExistingUser() throws Exception {

        Customer customer = new Customer();
        customer.setUsername("janKowalski");
        customer.setPassword("janKowalskipass");

        when(customerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(customer));

        UserDetails loggedUser = customerUserDetailsService.loadUserByUsername("janKowalski");

        assertEquals(customer.getUsername(), loggedUser.getUsername());
        assertEquals(customer.getPassword(), loggedUser.getPassword());
    }
}

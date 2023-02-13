package com.ecommerence.platform.service;

import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerService.class)
@ActiveProfiles("test")
public class CustomerServiceTest {


    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testRegisterUser() throws Exception {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setUsername("janKowalski");
        String password = "janKowalskipass";
        customerDto.setPassword(password);
        customerDto.setFirstName("Jan");
        customerDto.setLastName("Kowalski");
        customerDto.setEmail("janKowalski@mail.bg");
        customerDto.setPhone("123456789");
        customerDto.setAddress("ul. Kowalska 1");
        customerDto.setCity("Warszawa");

        CustomerDto registerDto = customerService.register(customerDto);

        assertEquals(customerDto, registerDto);

    }

}

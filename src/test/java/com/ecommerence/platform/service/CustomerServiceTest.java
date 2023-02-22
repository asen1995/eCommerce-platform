package com.ecommerence.platform.service;

import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.dto.OrderDto;
import com.ecommerence.platform.entity.Customer;
import com.ecommerence.platform.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerService.class)
@ActiveProfiles("test")
public class CustomerServiceTest {


    @Autowired
    private ICustomerService customerService;

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

    @Test
    public void testSearchCustomers() {

        final int page = 0;
        final int pageSize = 1;
        final String janKowalski = "janKowalski";

        Customer customer = new Customer();
        customer.setUsername("janKowalski");
        customer.setFirstName("Jan");

        Page<Customer> pageMock = new PageImpl<>(Arrays.asList(customer));

        when(customerRepository.findAll(  ((Specification) ArgumentMatchers.any() ) , (Pageable) ArgumentMatchers.any())).thenReturn(pageMock);

        final List<CustomerDto> customerDtos = customerService.searchCustomers(janKowalski, page, pageSize);

        assertEquals(1, customerDtos.size());
        assertEquals(janKowalski, customerDtos.get(0).getUsername());

    }
}

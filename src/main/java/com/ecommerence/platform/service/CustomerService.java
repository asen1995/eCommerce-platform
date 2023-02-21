package com.ecommerence.platform.service;

import com.ecommerence.platform.constants.RsqlConstants;
import com.ecommerence.platform.dto.CustomerDto;
import com.ecommerence.platform.entity.Customer;
import com.ecommerence.platform.repository.CustomerRepository;
import com.ecommerence.platform.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerDto register(CustomerDto customerDto) {

        Customer customer = new Customer();
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setCity(customerDto.getCity());
        customer.setCreatedDate(new Date());

        customerRepository.save(customer);

        return customerDto;
    }

    @Override
    public List<CustomerDto> searchCustomers(String search, Integer page, Integer pageSize) {

        Node rootNode = new RSQLParser().parse(RsqlConstants.CUSTOMER_GLOBAL_SEARCH_RSQL_QUERY.replace("searchString", search));
        Specification<Customer> spec = rootNode.accept(new CustomRsqlVisitor<>());

        Pageable pageable = PageRequest.of(page, pageSize);

        return customerRepository.findAll(spec, pageable)
                .stream()
                .map(customer -> {
                    CustomerDto customerDto = new CustomerDto();
                    customerDto.setFirstName(customer.getFirstName());
                    customerDto.setLastName(customer.getLastName());
                    customerDto.setAddress(customer.getAddress());
                    customerDto.setCity(customer.getCity());
                    customerDto.setPhone(customer.getPhone());
                    customerDto.setEmail(customer.getEmail());
                    return customerDto;
                }).collect(Collectors.toList());
    }
}

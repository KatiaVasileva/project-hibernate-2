package com.vasileva.service;

import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.entity.Customer;
import com.vasileva.repository.CustomerRepository;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(CustomerCreationRequest request) {
        return customerRepository.create(request);
    }
}

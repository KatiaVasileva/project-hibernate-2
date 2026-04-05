package com.vasileva.service;

import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.repository.CustomerRepository;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createCustomer(CustomerCreationRequest request) {
        customerRepository.create(request);
    }

    public void returnRentedFilm(int customerId, int rentalId) {
        customerRepository.returnRentedFilm(customerId, rentalId);
    }

    public void rentInventory(int customerId, int inventoryId, int staffId) {
        customerRepository.rentInventory(customerId, inventoryId, staffId);
    }
}

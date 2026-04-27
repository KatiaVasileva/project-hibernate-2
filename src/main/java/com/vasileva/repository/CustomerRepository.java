package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.entity.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;

public class CustomerRepository extends BaseRepository<Customer> {

    public CustomerRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Customer.class);
    }

    public Customer createCustomer(CustomerCreationRequest request, Address address) {
        Session session = getSession();
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .address(address)
                .active(true)
                .build();

        Store store = session.find(Store.class, request.getStoreId());
        if (store == null) {
            throw new EntityNotFoundException("Store with ID " + request.getStoreId() + " not found");
        }
        customer.setStore(store);
        session.persist(customer);
        session.flush();
        return customer;
    }
}

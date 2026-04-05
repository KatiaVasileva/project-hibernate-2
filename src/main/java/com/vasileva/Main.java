package com.vasileva;

import com.vasileva.config.NanoSpring;
import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.service.CustomerService;

public class Main {
    public static void main(String[] args) {

        CustomerService customerService = NanoSpring.find(CustomerService.class);

        CustomerCreationRequest request = CustomerCreationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("Broadway 5")
                .city("Moscow")
                .postalCode("123456")
                .country("Russian Federation")
                .storeId((short) 1)
                .district("Moscow")
                .phone("123456789")
                .build();

        customerService.createCustomer(request);

        customerService.returnRentedFilm(5, 13209);

        customerService.rentInventory(1, 12, 1);


    }
}

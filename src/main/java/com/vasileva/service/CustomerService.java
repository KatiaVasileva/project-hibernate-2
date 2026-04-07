package com.vasileva.service;

import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.entity.*;
import com.vasileva.repository.*;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;
    private final RentalRepository rentalRepository;
    private final StaffRepository staffRepository;
    private final InventoryRepository inventoryRepository;
    private final FilmRepository filmRepository;
    private final PaymentRepository paymentRepository;

    public CustomerService(CustomerRepository customerRepository,
                           CityRepository cityRepository,
                           AddressRepository addressRepository,
                           RentalRepository rentalRepository,
                           StaffRepository staffRepository,
                           InventoryRepository inventoryRepository, FilmRepository filmRepository, PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
        this.addressRepository = addressRepository;
        this.rentalRepository = rentalRepository;
        this.staffRepository = staffRepository;
        this.inventoryRepository = inventoryRepository;
        this.filmRepository = filmRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void createCustomer(CustomerCreationRequest request) {
        City city = cityRepository.findCityWithCountry(request.getCity(), request.getCountry());
        Address address = addressRepository.findOrCreateAddress(request, city);
        Customer customer = customerRepository.createCustomer(request, address);
        System.out.println("Customer successfully created with ID: " + customer.getId());
    }

    @Transactional
    public void returnRentedFilm(int rentalId) {
        Rental rental = rentalRepository.get(rentalId);
        rentalRepository.updateReturnDate(rental);
    }

    @Transactional
    public void rentInventory(int customerId, int inventoryId, int staffId) {
        Customer customer = customerRepository.get(customerId);
        Staff staff = staffRepository.get(staffId);
        Inventory inventory = inventoryRepository.get(inventoryId);

        if (inventoryRepository.isInventoryAvailable(inventoryId)) {
            Rental rental = rentalRepository.createRental(customer, staff, inventory);
            BigDecimal rentalRate = filmRepository.getRentalRate(inventoryId);
            paymentRepository.createPayment(customer, staff, rental, rentalRate);
            System.out.println("Inventory rented successfully");
        } else {
            System.out.println("Inventory is currently rented and not available");
        }
    }
}




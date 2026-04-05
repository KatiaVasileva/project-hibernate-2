package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.entity.*;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class CustomerRepository {
    private final SessionCreator sessionCreator;

    public CustomerRepository(SessionCreator sessionCreator) {
        this.sessionCreator = sessionCreator;
    }

    @Transactional
    public void create(CustomerCreationRequest request) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();

        try (session) {
            City city = findCityWithCountry(session, request.getCity(), request.getCountry());
            Address address = findOrCreateAddress(session, request, city);
            Customer customer = createCustomer(session, request, address);
            transaction.commit();
            System.out.println("Customer successfully created with ID: " + customer.getId());
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Transactional
    public void returnRentedFilm(int customerId, int rentalId) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        Rental rental;

        try (session) {
            Customer customer = session.find(Customer.class, customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer with ID " + customerId + " not found");
            }

            rental = session.find(Rental.class, rentalId);
            if (rental == null) {
                throw new IllegalArgumentException("Rental with ID " + rentalId + " not found");
            }

            if (rental.getReturnDate() == null) {
                rental.setReturnDate(LocalDateTime.now());
                rental.setLastUpdate(LocalDateTime.now());

                session.merge(rental);

                System.out.println("Film successfully returned by customer ID: " + customerId +
                        ", rental ID: " + rentalId);
            } else {
                System.out.println("Film has already been returned on " + rental.getReturnDate());
            }
            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }



    private City findCityWithCountry(Session session, String cityName, String countryName) {
        City city = session.createQuery(
                        "select c from City c join Country country on c.country.id = country.id where c.city = :cityName and country.country = :countryName", City.class)
                .setParameter("cityName", cityName)
                .setParameter("countryName", countryName)
                .uniqueResult();
        if (city == null) {
            throw new IllegalArgumentException("City not found: " + cityName);
        }
        return city;
    }

    private Address findOrCreateAddress(Session session, CustomerCreationRequest request, City city) {
        Address address = session.createQuery(
                        "select a from Address a where a.address = :address and a.city = :city", Address.class)
                .setParameter("address", request.getAddress())
                .setParameter("city", city)
                .uniqueResult();

        if (address == null) {
            address = Address.builder()
                    .address(request.getAddress())
                    .address2(request.getAddress2())
                    .district(request.getDistrict())
                    .city(city)
                    .postalCode(request.getPostalCode())
                    .phone(request.getPhone())
                    .lastUpdate(LocalDateTime.now())
                    .build();
            session.persist(address);
        }
        return address;
    }

    private Customer createCustomer(Session session, CustomerCreationRequest request, Address address) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .address(address)
                .active(true)
                .createDate(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        Store store = session.find(Store.class, request.getStoreId());
        if (store == null) {
            throw new IllegalArgumentException("Store with ID " + request.getStoreId() + " not found");
        }
        customer.setStore(store);

        session.persist(customer);
        return customer;
    }
}

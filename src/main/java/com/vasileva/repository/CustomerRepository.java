package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class CustomerRepository {
    private final SessionCreator sessionCreator;

    public CustomerRepository(SessionCreator sessionCreator) {
        this.sessionCreator = sessionCreator;
    }

    public Customer create(CustomerCreationRequest request) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();

        try (session; sessionCreator) {
            City city = findCityWithCountry(session, request.getCity(), request.getCountry());
            Address address = findOrCreateAddress(session, request, city);
            Customer customer = createCustomer(session, request, address);
            transaction.commit();
            return customer;
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

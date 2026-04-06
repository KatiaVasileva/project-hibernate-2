package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.entity.*;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
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

    @Transactional
    public void rentInventory(int customerId, int inventoryId, int staffId) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        Rental rental;

        try (session) {
            Customer customer = session.get(Customer.class, customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer with ID " + customerId + " not found");
            }

            Staff staff = session.get(Staff.class, staffId);
            if (staff == null) {
                throw new IllegalArgumentException("Staff with ID " + staffId + " not found");
            }

            Inventory inventory = session.get(Inventory.class, inventoryId);
            if (inventory == null) {
                throw new IllegalArgumentException("Inventory with ID " + inventoryId + " not found");
            }

            if (isInventoryAvailable(session, inventoryId)) {
                rental = Rental.builder()
                        .rentalDate(LocalDateTime.now())
                        .customer(customer)
                        .staff(staff)
                        .inventory(inventory)
                        .build();
                session.persist(rental);

                BigDecimal rental_rate = session.createQuery(
                        "select f.rentalRate " +
                                "from Inventory inv join inv.film f " +
                                "where inv.id = :inventoryID", BigDecimal.class)
                        .setParameter("inventoryID", inventoryId)
                        .uniqueResult();

                Payment payment = Payment.builder()
                        .customer(customer)
                        .staff(staff)
                        .rental(rental)
                        .amount(rental_rate)
                        .paymentDate(LocalDateTime.now())
                        .build();
                session.persist(payment);
                System.out.println("Inventory rented successfully");
            } else {
                System.out.println("Inventory is currently rented and not available");
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
                .build();

        Store store = session.find(Store.class, request.getStoreId());
        if (store == null) {
            throw new IllegalArgumentException("Store with ID " + request.getStoreId() + " not found");
        }
        customer.setStore(store);

        session.persist(customer);
        return customer;
    }

    private boolean isInventoryAvailable(Session session, int inventoryId) {
        Rental latestRental = session.createQuery(
                        "select r from Rental r where inventory.id = :inventoryId " +
                                "order by rentalDate desc", Rental.class)
                .setParameter("inventoryId", inventoryId)
                .setMaxResults(1)
                .uniqueResult();
        return latestRental == null || latestRental.getReturnDate() != null;
    }
}

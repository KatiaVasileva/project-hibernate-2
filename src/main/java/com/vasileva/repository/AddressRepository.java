package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.entity.Address;
import com.vasileva.entity.City;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AddressRepository extends BaseRepository<Address> {

    public AddressRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Address.class);
    }

    @Transactional
    public Address findOrCreateAddress(CustomerCreationRequest request, City city) {
        try (Session session = getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            Address address = session.createQuery(
                            "select a from Address a " +
                                    "where a.address = :address and a.city = :city", Address.class)
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
                session.flush();
            }
            transaction.commit();
            return address;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

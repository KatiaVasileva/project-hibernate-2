package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.entity.Address;
import com.vasileva.entity.City;
import org.hibernate.Session;

public class AddressRepository extends BaseRepository<Address> {

    public AddressRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Address.class);
    }

    public Address findOrCreateAddress(CustomerCreationRequest request, City city) {
        Session session = getSession();
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
        return address;
    }
}

package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Country;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;

import java.util.Optional;

public class CountryRepository extends BaseRepository<Country> {

    public CountryRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Country.class);
    }

    public Country getByName(String countryName) {
        Session session = getCurrentSession();
        Country country = session.createQuery(
                        "select co from Country co where co.country = :countryName", Country.class)
                .setParameter("countryName", countryName)
                .uniqueResult();

        return Optional.ofNullable(country)
                .orElseThrow(() -> new EntityNotFoundException("Country not found: " + countryName));
    }
}


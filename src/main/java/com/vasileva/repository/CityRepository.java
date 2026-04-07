package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.City;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

public class CityRepository extends BaseRepository<City> {

    public CityRepository(SessionCreator sessionCreator) {
        super(sessionCreator, City.class);
    }

    public City findCityWithCountry(String cityName, String countryName) {
        Session session = getCurrentSession();
        Query<City> query = session.createQuery(
                        "select c from City c " +
                                "join fetch c.country co  " +
                                "where c.city = :cityName and co.country = :countryName", City.class)
                .setParameter("cityName", cityName)
                .setParameter("countryName", countryName);
        return Optional.ofNullable(query.uniqueResult())
                .orElseThrow(() -> new EntityNotFoundException("City not found: " + cityName));
    }
}

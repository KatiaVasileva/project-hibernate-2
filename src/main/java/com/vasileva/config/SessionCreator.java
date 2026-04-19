package com.vasileva.config;

import com.vasileva.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

public class SessionCreator implements Closeable {

    private final SessionFactory sessionFactory;

    public SessionCreator() throws IOException {
        Configuration configuration = new Configuration();
        Properties properties = configuration.getProperties();
        properties.load(SessionFactory.class.getResourceAsStream(
                "/application.properties"));
        configuration.addProperties(properties);
        configuration.addAnnotatedClass(Film.class);
        configuration.addAnnotatedClass(Actor.class);
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(FilmText.class);
        configuration.addAnnotatedClass(Language.class);
        configuration.addAnnotatedClass(Country.class);
        configuration.addAnnotatedClass(City.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.addAnnotatedClass(Store.class);
        configuration.addAnnotatedClass(Staff.class);
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Inventory.class);
        configuration.addAnnotatedClass(Rental.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        sessionFactory = configuration.buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public void close() {
        sessionFactory.close();
    }
}

package com.vasileva.config;

import com.vasileva.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionCreator {

    private final SessionFactory sessionFactory;

    public SessionCreator() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
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
        sessionFactory = configuration.buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}

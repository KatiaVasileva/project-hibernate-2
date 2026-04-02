package com.vasileva.config;

import com.vasileva.entity.Actor;
import com.vasileva.entity.Category;
import com.vasileva.entity.Film;
import com.vasileva.entity.FilmText;
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
        sessionFactory = configuration.buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}

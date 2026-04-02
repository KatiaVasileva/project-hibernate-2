package com.vasileva;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Film;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        SessionCreator sessionCreator = new SessionCreator();
        Session session = sessionCreator.getSession();

        session.beginTransaction();
        Film film = session.find(Film.class, 1);
        System.out.println(film);
        session.getTransaction().commit();


    }
}

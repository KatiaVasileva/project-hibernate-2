package com.vasileva;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Actor;
import com.vasileva.entity.Category;
import com.vasileva.entity.Film;
import org.hibernate.Session;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        SessionCreator sessionCreator = new SessionCreator();
        Session session = sessionCreator.getSession();

        session.beginTransaction();
        Film film = session.find(Film.class, 1);
        System.out.println(film);
        Actor actor = session.find(Actor.class, 1);
        System.out.println(actor);
        List<Film> films = actor.getFilms();
        films.forEach(System.out::println);
        List<Category> categories = film.getCategories();
        categories.forEach(System.out::println);
        session.getTransaction().commit();


    }
}

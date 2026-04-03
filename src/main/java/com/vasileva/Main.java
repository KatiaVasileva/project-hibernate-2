package com.vasileva;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.*;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        SessionCreator sessionCreator = new SessionCreator();
        Session session = sessionCreator.getSession();

        session.beginTransaction();

        Film film = session.find(Film.class, 1);
        System.out.println(film);

        Actor actor = session.find(Actor.class, 2);
        System.out.println(actor);

        Set<Film> films = actor.getFilms();
        films.forEach(System.out::println);

        List<Category> categories = film.getCategories();
        categories.forEach(System.out::println);

        FilmText text = session.find(FilmText.class, 1);
        System.out.println(text);

        Language language = film.getLanguage();
        System.out.println(language + ": Film " + film);

        Country country = session.find(Country.class, 1);
        System.out.println(country);

        City city = session.find(City.class, 1);
        System.out.println(city);

        Address address = session.find(Address.class, 1);
        System.out.println(address);

        Store store = session.find(Store.class, 1);
        System.out.println(store);

        Staff staff = session.find(Staff.class, 1);
        System.out.println(staff);

        Customer customer = session.find(Customer.class, 1);
        System.out.println(customer);

        Inventory inventory = session.find(Inventory.class, 1);
        System.out.println(inventory);

        Payment payment = session.find(Payment.class, 1);
        System.out.println(payment);

        Rental rental = session.find(Rental.class, 1);
        System.out.println(rental);

        session.getTransaction().commit();


    }
}

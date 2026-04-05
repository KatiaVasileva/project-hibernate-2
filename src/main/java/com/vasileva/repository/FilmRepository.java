package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.*;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Set;

public class FilmRepository {
    private final SessionCreator sessionCreator;

    public FilmRepository(SessionCreator sessionCreator) {
        this.sessionCreator = sessionCreator;
    }

    @Transactional
    public void addNewFilm(
            String title,
            String description,
            Year releaseYear,
            byte rentalDuration,
            BigDecimal rentalRate,
            BigDecimal replacementCost,
            Rating rating,
            List<Integer> actorIds,
            int categoryId,
            int languageId,
            short length,
            Set<SpecialFeature> specialFeatures
    ) {
        Session session = sessionCreator.getSession();
        Transaction transaction = session.beginTransaction();
        Film film;

        try (session) {
            Language language = session.find(Language.class, languageId);
            if (language == null) {
                throw new IllegalArgumentException("Language with ID " + languageId + " not found");
            }

            Category category = session.find(Category.class, categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Category with ID " + categoryId + " not found");
            }

            if (actorIds != null && !actorIds.isEmpty()) {
                for (Integer actorId : actorIds) {
                    Actor actor = session.find(Actor.class, actorId);
                    if (actor == null) {
                        throw new IllegalArgumentException("Actor with ID " + actorId + " not found");
                    }
                }
            }

            film = Film.builder()
                    .title(title)
                    .description(description)
                    .releaseYear(releaseYear)
                    .rentalRate(rentalRate)
                    .rentalDuration(rentalDuration)
                    .replacementCost(replacementCost)
                    .rating(rating)
                    .language(language)
                    .length(length)
                    .lastUpdate(LocalDateTime.now())
                    .specialFeatures(specialFeatures)
                    .build();
            session.persist(film);

            session.flush();

            int filmId = film.getId();

            if (actorIds != null && !actorIds.isEmpty()) {
                String sqlActor = "INSERT INTO movie.film_actor (actor_id, film_id, last_update) VALUES (?, ?, ?)";
                for (Integer actorId : actorIds) {
                    session.createNativeQuery(sqlActor, Actor.class)
                            .setParameter(1, actorId)
                            .setParameter(2, filmId)
                            .setParameter(3, LocalDateTime.now())
                            .executeUpdate();
                }
            }

            String sqlCategory = "INSERT INTO movie.film_category (film_id, category_id, last_update) VALUES (?, ?, ?)";
            session.createNativeQuery(sqlCategory, Category.class)
                    .setParameter(1, filmId)
                    .setParameter(2, categoryId)
                    .setParameter(3, LocalDateTime.now())
                    .executeUpdate();

            List<Store> stores = session.createQuery("select s from Store s", Store.class).list();
            for (Store store : stores) {
                Inventory inventory = Inventory.builder()
                        .film(film)
                        .store(store)
                        .lastUpdate(LocalDateTime.now())
                        .build();
                session.persist(inventory);
            }

            transaction.commit();

            System.out.println("New film added successfully:");
            System.out.println("- Title: " + title);
            System.out.println("- Film ID: " + filmId);
            System.out.println("- Actors: " + (actorIds != null ? actorIds.size() : 0));
            System.out.println("- Category: " + category.getName());
            System.out.println("- Language: " + language.getName());
            System.out.println("- Available in " + stores.size() + " stores");

        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}

package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.dto.FilmCreationRequest;
import com.vasileva.entity.*;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FilmRepository extends BaseRepository<Film> {

    public FilmRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Film.class);
    }

    @Transactional
    public Film createFilm(FilmCreationRequest request, Language language) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Film film = Film.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .releaseYear(request.getReleaseYear())
                .rentalRate(request.getRentalRate())
                .rentalDuration(request.getRentalDuration())
                .replacementCost(request.getReplacementCost())
                .rating(request.getRating())
                .language(language)
                .length(request.getLength())
                .specialFeatures(request.getSpecialFeatures())
                .build();
        session.persist(film);
        session.flush();
        transaction.commit();
        return film;
    }

    @Transactional
    public void createActorsForFilm(List<Integer> actorIds, int filmId) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String sqlActor = "INSERT INTO movie.film_actor (actor_id, film_id, last_update) VALUES (?, ?, ?)";
        for (Integer actorId : actorIds) {
            session.createNativeQuery(sqlActor, Actor.class)
                    .setParameter(1, actorId)
                    .setParameter(2, filmId)
                    .setParameter(3, LocalDateTime.now())
                    .executeUpdate();
        }
        session.flush();
        transaction.commit();
    }

    @Transactional
    public void createCategoryForFilm(int filmId, int categoryId) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        String sqlCategory = "INSERT INTO movie.film_category (film_id, category_id, last_update) VALUES (?, ?, ?)";
        session.createNativeQuery(sqlCategory, Category.class)
                .setParameter(1, filmId)
                .setParameter(2, categoryId)
                .setParameter(3, LocalDateTime.now())
                .executeUpdate();
        session.flush();
        transaction.commit();
    }

    public BigDecimal getRentalRate(int inventoryId) {
        Session session = getCurrentSession();
        BigDecimal rentalRate = session.createQuery(
                        "SELECT f.rentalRate " +
                                "FROM Inventory inv JOIN inv.film f " +
                                "WHERE inv.id = :inventoryID", BigDecimal.class)
                .setParameter("inventoryID", inventoryId)
                .uniqueResult();
        return rentalRate != null ?  rentalRate : BigDecimal.valueOf(4.99);
    }
}

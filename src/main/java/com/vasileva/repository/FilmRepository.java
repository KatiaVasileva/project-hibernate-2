package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.dto.FilmCreationRequest;
import com.vasileva.entity.*;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FilmRepository extends BaseRepository<Film> {

    public FilmRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Film.class);
    }

    public Film createFilm(FilmCreationRequest request, Language language) {
        Session session = getSession();
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
        return film;
    }

    public void createActorsForFilm(List<Integer> actorIds, int filmId) {
        Session session = getSession();
        String sqlActor = "INSERT INTO movie.film_actor (actor_id, film_id, last_update) VALUES (?, ?, ?)";
        for (Integer actorId : actorIds) {
            session.createNativeQuery(sqlActor, Actor.class)
                    .setParameter(1, actorId)
                    .setParameter(2, filmId)
                    .setParameter(3, LocalDateTime.now())
                    .executeUpdate();
        }
        session.flush();
    }

    public void createCategoryForFilm(int filmId, int categoryId) {
        Session session = getSession();
        String sqlCategory = "INSERT INTO movie.film_category (film_id, category_id, last_update) VALUES (?, ?, ?)";
        session.createNativeQuery(sqlCategory, Category.class)
                .setParameter(1, filmId)
                .setParameter(2, categoryId)
                .setParameter(3, LocalDateTime.now())
                .executeUpdate();
        session.flush();
    }

    public BigDecimal getRentalRate(int inventoryId) {
        Session session = getSession();
        BigDecimal rentalRate = session.createQuery(
                        "SELECT f.rentalRate " +
                                "FROM Inventory inv JOIN inv.film f " +
                                "WHERE inv.id = :inventoryID", BigDecimal.class)
                .setParameter("inventoryID", inventoryId)
                .uniqueResult();
        return rentalRate != null ?  rentalRate : BigDecimal.valueOf(4.99);
    }
}

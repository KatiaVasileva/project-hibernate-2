package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Session;

import java.util.Optional;

public class BaseRepository<T> implements Repository<T> {
    private final SessionCreator sessionCreator;
    private final Class<T> entityClass;

    public BaseRepository(SessionCreator sessionCreator, Class<T> entityClass) {
        this.sessionCreator = sessionCreator;
        this.entityClass = entityClass;
    }

    @Override
    public T get(long id) {
        Session session = sessionCreator.getSession();
        return Optional.ofNullable(session.get(entityClass, id))
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));

    }

    @Transactional
    protected Session getCurrentSession() {
        return sessionCreator.getSession();
    }

}

package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;

import java.util.List;
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
        return Optional.ofNullable(getSession().get(entityClass, id))
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));
    }

    @Override
    public List<T> getAll() {
        return getSession().createQuery("SELECT e FROM %s e".formatted(entityClass.getName()), entityClass).list();
    }

    @Override
    public void create(T entity) {
        getSession().persist(entity);
    }

    @Override
    public void update(T entity) {
        getSession().merge(entity);
    }

    @Override
    public void delete(long id) {
        Session session = getSession();
        T entity = getSession().get(entityClass, id);
        session.remove(entity);
    }

    protected Session getSession() {
        return sessionCreator.getCurrentSession();
    }
}

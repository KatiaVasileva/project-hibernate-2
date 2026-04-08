package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        Session session = sessionCreator.getSession();
        return Optional.ofNullable(session.get(entityClass, id))
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));

    }

    @Override
    public List<T> getAll() {
        Session session = sessionCreator.getSession();
        return session.createQuery("SELECT e FROM %s e".formatted(entityClass.getName()), entityClass).list();
    }

    @Override
    @Transactional
    public void create(T entity) {
        try (Session session = sessionCreator.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            session.flush();
            transaction.commit();
        }
    }

    @Override
    @Transactional
    public void update(T entity) {
        try (Session session = sessionCreator.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(entity);
            session.flush();
            transaction.commit();
        }
    }

    @Override
    @Transactional
    public void delete(long id) {
        try (Session session = sessionCreator.getSession()) {
            Transaction transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            session.remove(entity);
            session.flush();
            transaction.commit();
        }
    }

    @Transactional
    protected Session getCurrentSession() {
        return sessionCreator.getSession();
    }

}

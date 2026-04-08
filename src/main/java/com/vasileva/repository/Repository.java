package com.vasileva.repository;

import java.util.List;

public interface Repository<T> {

    T get(long id);

    List<T> getAll();

    void create(T entity);

    void update(T entity);

    void delete(long id);

}

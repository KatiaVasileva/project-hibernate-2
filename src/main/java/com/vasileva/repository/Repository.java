package com.vasileva.repository;

public interface Repository<T> {

    T get(long id);

}

package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Store;

public class StoreRepository extends BaseRepository<Store> {

    public StoreRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Store.class);
    }
}

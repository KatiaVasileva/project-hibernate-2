package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Category;

public class CategoryRepository extends BaseRepository<Category> {

    public CategoryRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Category.class);
    }
}

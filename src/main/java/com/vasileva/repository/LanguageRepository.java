package com.vasileva.repository;

import com.vasileva.config.SessionCreator;
import com.vasileva.entity.Language;

public class LanguageRepository extends BaseRepository<Language> {

    public LanguageRepository(SessionCreator sessionCreator) {
        super(sessionCreator, Language.class);
    }
}

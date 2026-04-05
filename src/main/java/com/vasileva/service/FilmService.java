package com.vasileva.service;

import com.vasileva.entity.Rating;
import com.vasileva.entity.SpecialFeature;
import com.vasileva.repository.FilmRepository;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Set;

public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public void addNewFilm(
            String title,
            String description,
            Year releaseYear,
            byte rentalDuration,
            BigDecimal rentalRate,
            BigDecimal replacementCost,
            Rating rating,
            List<Integer> actorIds,
            int categoryId,
            int languageId,
            short length,
            Set<SpecialFeature> specialFeatures
    ) {
        filmRepository.addNewFilm(
                title,
                description,
                releaseYear,
                rentalDuration,
                rentalRate,
                replacementCost,
                rating,
                actorIds,
                categoryId,
                languageId,
                length,
                specialFeatures);
    }
}

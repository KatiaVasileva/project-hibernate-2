package com.vasileva.service;

import com.vasileva.dto.FilmCreationRequest;
import com.vasileva.entity.*;
import com.vasileva.repository.*;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public class FilmService {
    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final InventoryRepository inventoryRepository;

    public FilmService(FilmRepository filmRepository,
                       LanguageRepository languageRepository,
                       CategoryRepository categoryRepository,
                       StoreRepository storeRepository,
                       InventoryRepository inventoryRepository) {
        this.filmRepository = filmRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void addNewFilm(FilmCreationRequest request) {
        Language language = languageRepository.get(request.getLanguageId());
        Category category = categoryRepository.get(request.getCategoryId());

        Film film = filmRepository.createFilm(request, language);

        List<Integer> actorsIds = request.getActorIds();
        if (actorsIds != null && !actorsIds.isEmpty()) {
            filmRepository.createActorsForFilm(actorsIds, film.getId());
        }

        filmRepository.createCategoryForFilm(film.getId(), category.getId());

        List<Store> stores = storeRepository.getAll();
        for (Store store : stores) {
            inventoryRepository.createInventory(film, store);
        }

        System.out.println("New film added successfully:");
        System.out.println("- Title: " + film.getTitle());
        System.out.println("- Film ID: " + film.getId());
        System.out.println("- Language: " + film.getLanguage().getName());
        System.out.println("- Available in " + stores.size() + " stores");
    }
}

package com.vasileva;

import com.vasileva.config.NanoSpring;
import com.vasileva.dto.CustomerCreationRequest;
import com.vasileva.dto.FilmCreationRequest;
import com.vasileva.entity.Rating;
import com.vasileva.entity.SpecialFeature;
import com.vasileva.service.CustomerService;
import com.vasileva.service.FilmService;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        CustomerService customerService = NanoSpring.find(CustomerService.class);
        FilmService filmService = NanoSpring.find(FilmService.class);

        CustomerCreationRequest request = CustomerCreationRequest.builder()
                .firstName("Mary")
                .lastName("Smith")
                .email("mary.smith@example.com")
                .address("Broadway 15")
                .city("Baku")
                .postalCode("123456")
                .country("Azerbaijan")
                .storeId((short) 2)
                .district("2")
                .phone("123456789")
                .build();

        customerService.createCustomer(request);

        customerService.returnRentedFilm(16068);

        customerService.rentInventory(1, 20, 1);

        List<Integer> actorIds = Arrays.asList(1, 2, 3);
        int categoryId = 5;
        int languageId = 1;
        String year = "2024";
        Set<SpecialFeature> specialFeatures = Set.of(SpecialFeature.TRAILERS);
        FilmCreationRequest filmCreationRequest = FilmCreationRequest.builder()
                .title("Cyber Guardians")
                .description("In a futuristic world overrun by AI, a group of hackers fights to restore human control.")
                .releaseYear(Year.parse(year))
                .rentalDuration((byte) 3)
                .rentalRate(BigDecimal.valueOf(4.99))
                .replacementCost(BigDecimal.valueOf(19.99))
                .rating(Rating.PG13)
                .actorIds(actorIds)
                .categoryId(categoryId)
                .languageId(languageId)
                .length((short) 100)
                .specialFeatures(specialFeatures)
                .build();

        filmService.addNewFilm(filmCreationRequest);
    }
}

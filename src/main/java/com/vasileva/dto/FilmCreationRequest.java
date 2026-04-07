package com.vasileva.dto;

import com.vasileva.entity.Rating;
import com.vasileva.entity.SpecialFeature;
import lombok.*;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmCreationRequest {
    private String title;
    private String description;
    private Year releaseYear;
    private byte rentalDuration;
    private BigDecimal rentalRate;
    private BigDecimal replacementCost;
    private Rating rating;
    private List<Integer> actorIds;
    private int categoryId;
    private int languageId;
    private short length;
    private Set<SpecialFeature> specialFeatures;
}

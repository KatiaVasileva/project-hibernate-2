package com.vasileva.entity;

import com.vasileva.entity.converter.RatingConverter;
import com.vasileva.entity.converter.SpecialFeatureConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"actors", "categories"})

@Entity
@Table(name = "film")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private short id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "release_year", columnDefinition = "YEAR")
    private Year releaseYear;

    @Column(name = "language_id", nullable = false)
    private byte languageId;

    @Column(name = "original_language_id")
    private Byte originalLanguageId;

    @Column(name = "rental_duration", nullable = false)
    private byte rentalDuration = 3;

    @Column(name = "rental_rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal rentalRate = BigDecimal.valueOf(4.99);

    private short length;

    @Column(name = "replacement_cost", nullable = false, precision = 5, scale = 2)
    private BigDecimal replacementCost = BigDecimal.valueOf(19.99);

    @Convert(converter = RatingConverter.class)
    @Column(columnDefinition = "ENUM('G', 'PG', 'PG-13', 'R', 'NC-17')")
    private Rating rating = Rating.G;

    @Column(name = "special_features", columnDefinition = "SET('Trailers','Commentaries','Deleted Scenes','Behind the Scenes')")
    @Convert(converter = SpecialFeatureConverter.class)
    private Set<SpecialFeature> specialFeatures = new HashSet<>();

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;

    @ManyToMany
    @JoinTable(
            name = "film_category",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}

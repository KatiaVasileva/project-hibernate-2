package com.vasileva.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "film")

@Entity
@Table(schema = "movie", name = "film_text")
public class FilmText {
    @Id
    @Column(name = "film_id")
    private Short filmId;

    @Column(nullable = false)
    private String title;

    private String description;

    @OneToOne
    @JoinColumn(name = "film_id", insertable = false, updatable = false)
    private Film film;
}

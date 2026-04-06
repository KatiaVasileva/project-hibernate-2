package com.vasileva.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"filmsAsPrimary", "filmsAsOriginal"})

@Entity
@Table(schema = "movie", name = "language")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Byte id;

    @Column(nullable = false, length = 20, columnDefinition = "CHAR(20)")
    private String name;

    @Column(name = "last_update", nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    private List<Film> filmsAsPrimary;

    @OneToMany(mappedBy = "originalLanguage", fetch = FetchType.LAZY)
    private List<Film> filmsAsOriginal;
}

package com.vasileva.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "cities")

@Entity
@Table(schema = "movie", name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "country_id")
    private Short id;

    @Column(nullable = false, length = 50)
    private String country;

    @Column(name = "last_update", nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<City> cities;
}

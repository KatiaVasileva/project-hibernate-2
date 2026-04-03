package com.vasileva.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"picture", "payments", "rentals", "store"})

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Byte id;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] picture;

    @Column(length = 20)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean active = true;

    @Column(nullable = false, length = 16)
    private String username;

    @Column(length = 40)
    private String password;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "staff")
    private List<Payment> payments;

    @OneToMany(mappedBy = "staff")
    private List<Rental> rentals;
}

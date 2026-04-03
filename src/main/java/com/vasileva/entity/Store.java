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
@ToString(exclude = {"staffMembers", "customers", "inventory"})

@Entity
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Byte id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_staff_id", unique = true, nullable = false)
    private Staff manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "store")
    private List<Staff> staffMembers;

    @OneToMany(mappedBy = "store")
    private List<Customer> customers;

    @OneToMany(mappedBy = "store")
    private List<Inventory> inventory;
}

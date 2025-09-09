package dev.chadinasser.hamsterpos.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @PrePersist
    @PreUpdate
    private void ensureDefaults() {
        if (price == null) price = 0.0;
        if (stock == null) stock = 0;
    }
}

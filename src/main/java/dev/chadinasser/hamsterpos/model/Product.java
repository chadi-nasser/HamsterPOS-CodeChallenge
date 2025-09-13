package dev.chadinasser.hamsterpos.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.stock = 0; // Default stock to 0 if not provided
    }

    @PrePersist
    @PreUpdate
    private void ensureDefaults() {
        if (price == null) price = BigDecimal.ZERO;
        if (stock == null) stock = 0;
    }
}

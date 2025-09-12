package dev.chadinasser.hamsterpos.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    @Transient
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @PrePersist
    @PreUpdate
    private void ensureDefaults() {
        if (status == null) status = OrderStatus.PENDING;
        if (items == null) items = new ArrayList<>();
    }

    public Double getTotalPrice() {
        if (items == null) return 0.0;
        return items.stream()
                .mapToDouble(i -> (i.getUnitPrice() != null ? i.getUnitPrice() : 0.0)
                        * (i.getQuantity() != null ? i.getQuantity() : 0))
                .sum();
    }
}
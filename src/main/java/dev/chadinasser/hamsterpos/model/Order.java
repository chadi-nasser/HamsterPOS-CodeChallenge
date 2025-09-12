package dev.chadinasser.hamsterpos.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
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

    @Formula("(select coalesce(sum(oi.unit_price * oi.quantity), 0) from order_items oi where oi.order_id = id)")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @PrePersist
    @PreUpdate
    private void ensureDefaults() {
        if (status == null) status = OrderStatus.PENDING;
        if (items == null) items = new ArrayList<>();
    }
}
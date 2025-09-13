package dev.chadinasser.hamsterpos.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private List<OrderItem> items = new ArrayList<>();
    @Column(name = "total_amount", nullable = false)
    @Setter(AccessLevel.NONE)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    @Transient
    private LocalDateTime orderedAt = LocalDateTime.now();

    @PreUpdate
    private void preUpdate() {
        if (items == null) items = new ArrayList<>();
        totalAmount = calculateTotalAmount();
    }

    @PrePersist
    private void prePersist() {
        if (status == null) status = OrderStatus.PENDING;
        if (orderedAt == null) orderedAt = LocalDateTime.now();
        preUpdate();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public BigDecimal calculateTotalAmount() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public enum OrderStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }
}
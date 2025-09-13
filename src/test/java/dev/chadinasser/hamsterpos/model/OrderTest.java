package dev.chadinasser.hamsterpos.model;

import dev.chadinasser.hamsterpos.model.Order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        order = new Order();
        product1 = new Product("Burger", new BigDecimal("5.99"));
        product1.setId(UUID.randomUUID());
        product2 = new Product("Pizza", new BigDecimal("8.50"));
        product2.setId(UUID.randomUUID());
    }

    @Test
    void testOrderCreation() {
        assertNotNull(order);
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
        assertNotNull(order.getOrderedAt());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void testAddItemToOrder() {
        OrderItem item = new OrderItem(product1, 2); // 2 Burgers
        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals(order, item.getOrder());
        assertEquals(new BigDecimal("11.98"), order.calculateTotalAmount());
    }

    @Test
    void testCalculateTotal() {
        OrderItem item1 = new OrderItem(product1, 2); // 5.99 * 2 = 11.98
        OrderItem item2 = new OrderItem(product2, 1); // 8.50 * 1 = 8.50

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(new BigDecimal("20.48"), order.calculateTotalAmount());
    }

    @Test
    void testEmptyOrderTotal() {
        assertEquals(BigDecimal.ZERO, order.calculateTotalAmount());
    }
}

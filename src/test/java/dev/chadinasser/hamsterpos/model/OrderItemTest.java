package dev.chadinasser.hamsterpos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderItemTest {

    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        product = new Product("Test Product", new BigDecimal("10.00"));
        product.setId(UUID.randomUUID());
        orderItem = new OrderItem(product, 3);
    }

    @Test
    void testOrderItemCreation() {
        assertNotNull(orderItem);
        assertEquals(product, orderItem.getProduct());
        assertEquals(Integer.valueOf(3), orderItem.getQuantity());
        assertEquals(new BigDecimal("10.00"), orderItem.getUnitPrice());
    }

    @Test
    void testSubtotalCalculation() {
        BigDecimal expectedSubtotal = new BigDecimal("30.00");
        assertEquals(expectedSubtotal, orderItem.getSubtotal());
    }

    @Test
    void testOrderItemWithDifferentQuantity() {
        OrderItem item = new OrderItem(product, 5);
        assertEquals(new BigDecimal("50.00"), item.getSubtotal());
    }

    @Test
    void testOrderItemPriceCapture() {
        // Price should be captured at creation time
        assertEquals(product.getPrice(), orderItem.getUnitPrice());

        // Even if product price changes, order item price should remain the same
        product.setPrice(new BigDecimal("15.00"));
        assertEquals(new BigDecimal("10.00"), orderItem.getUnitPrice());
    }
}

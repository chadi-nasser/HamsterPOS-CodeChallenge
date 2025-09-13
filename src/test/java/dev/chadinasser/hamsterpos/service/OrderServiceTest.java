package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.dto.PaginationParams;
import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.model.Order.OrderStatus;
import dev.chadinasser.hamsterpos.model.Product;
import dev.chadinasser.hamsterpos.model.Role;
import dev.chadinasser.hamsterpos.model.Role.RoleType;
import dev.chadinasser.hamsterpos.model.User;
import dev.chadinasser.hamsterpos.repo.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    private final List<Order> saved = new ArrayList<>();
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private ProductService productService;
    @InjectMocks
    private OrderService orderService;
    private Product mockProduct;

    private User getDummyUser() {
        User currentUser = new User();
        currentUser.setId(UUID.randomUUID());
        currentUser.setEmail("me@example.com");
        Role currentRole = new Role();
        currentRole.setName(RoleType.USER);
        currentUser.setRole(currentRole);
        return currentUser;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockProduct = new Product("Test Product", new BigDecimal("10.00"));
        mockProduct.setId(UUID.randomUUID());

        // Stub save(...) to assign an ID and keep an in‑memory store
        when(orderRepo.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            if (o.getId() == null) o.setId(UUID.randomUUID());
            saved.add(o);
            return o;
        });

        // Stub findAll(...) to return the in‑memory store
        when(orderRepo.findAll(any(Pageable.class)))
                .thenAnswer(inv -> new PageImpl<>(new ArrayList<>(saved), inv.getArgument(0), saved.size()));
    }

    @Test
    void testCreateOrder() {
        Order newOrder = new Order();
        User currentUser = getDummyUser();

        Order order = orderService.createOrder(newOrder, currentUser);

        assertNotNull(order);
        assertNotNull(order.getId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
    }

    @Test
    void testGetAllOrders() {
        User currentUser = getDummyUser();
        orderService.createOrder(new Order(), currentUser);
        orderService.createOrder(new Order(), currentUser);

        Page<Order> orders = orderService.findAll(new PaginationParams());

        assertEquals(2, orders.getTotalElements());
    }
}
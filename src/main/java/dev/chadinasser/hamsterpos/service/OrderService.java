package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.dto.PaginationParams;
import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.model.Product;
import dev.chadinasser.hamsterpos.model.User;
import dev.chadinasser.hamsterpos.repo.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final ProductService productService;

    OrderService(OrderRepo orderRepo, ProductService productService) {
        this.orderRepo = orderRepo;
        this.productService = productService;
    }

    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    @Transactional
    public Order createOrder(Order order, User currentUser) {
        order.setUser(currentUser);
        order.getItems().forEach(item -> {
            Product currentProduct = productService.findById(item.getProduct().getId());
            item.setUnitPrice(currentProduct.getPrice());
            item.setOrder(order);
            productService.decreaseStock(currentProduct.getId(), item.getQuantity());
        });
        return orderRepo.save(order);
    }

    public Page<Order> findAllByUserId(UUID userId, PaginationParams paginationParams) {
        try {
            return orderRepo.findAllByUser_Id(userId, paginationParams.toPageable());
        } catch (PropertyReferenceException e) {
            throw new IllegalArgumentException("Invalid pagination parameters: sortBy=" + e.getPropertyName());
        }
    }
}

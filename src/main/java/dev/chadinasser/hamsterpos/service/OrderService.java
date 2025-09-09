package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.repo.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepo orderRepo;

    OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Order findById(UUID id) {
        return orderRepo.findById(id).orElse(null);
    }

    public Order findByIdWithItems(UUID id) {
        return orderRepo.findByIdWithItems(id).orElse(null);
    }

    public Order createOrder(Order order) {
        return orderRepo.save(order);
    }
}

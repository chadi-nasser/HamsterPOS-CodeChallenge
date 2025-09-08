package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.repo.OrderRepo;
import org.springframework.stereotype.Service;

@Service
class OrderService {
    private final OrderRepo orderRepo;

    OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }
}

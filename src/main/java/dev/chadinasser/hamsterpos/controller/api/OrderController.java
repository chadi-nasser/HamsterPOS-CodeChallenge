package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.OrderDto;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
class OrderController {
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseDto<OrderDto> createOrder(@Valid @RequestBody OrderDto order) {
        Order newOrder = orderService.createOrder(order.toEntity());
        return new ResponseDto<>(HttpStatus.CREATED, new OrderDto().fromEntity(newOrder));
    }
}

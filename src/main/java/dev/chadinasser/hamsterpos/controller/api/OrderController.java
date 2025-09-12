package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.OrderDto;
import dev.chadinasser.hamsterpos.dto.PaginationParams;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.mapper.OrderMapper;
import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.model.User;
import dev.chadinasser.hamsterpos.service.OrderService;
import dev.chadinasser.hamsterpos.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    OrderController(OrderService orderService, UserService userService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public ResponseDto<OrderDto> createOrder(@Valid @RequestBody OrderDto order) {
        User currentUser = userService.getAuthenticatedUser();
        Order newOrder = orderService.createOrder(orderMapper.toEntity(order), currentUser);
        return new ResponseDto<>(HttpStatus.CREATED, orderMapper.toDto(newOrder));
    }

    @GetMapping("/me")
    public ResponseDto<List<OrderDto>> getOrder(@Valid @ModelAttribute PaginationParams paginationParams, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid pagination parameters: " + bindingResult.getFieldErrors().stream().map(FieldError::getField).toList());
        }
        User currentUser = userService.getAuthenticatedUser();
        List<Order> orders = orderService.findAllByUserId(currentUser.getId(), paginationParams);
        List<OrderDto> orderDtos = orders.stream().map(orderMapper::toDto).toList();
        return new ResponseDto<>(HttpStatus.OK, orderDtos);
    }
}

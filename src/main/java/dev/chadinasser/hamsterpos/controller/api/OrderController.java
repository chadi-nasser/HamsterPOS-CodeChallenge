package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.OrderDto;
import dev.chadinasser.hamsterpos.dto.PagedDto;
import dev.chadinasser.hamsterpos.dto.PaginationParams;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.mapper.OrderMapper;
import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.model.User;
import dev.chadinasser.hamsterpos.service.OrderService;
import dev.chadinasser.hamsterpos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Tag(
        name = "Order",
        description = "Endpoints for managing orders"
)
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
    @Operation(
            summary = "Create a new order",
            description = "Creates a new order for the current authenticated user"
    )
    public ResponseDto<OrderDto> createOrder(@Valid @RequestBody OrderDto order) {
        User currentUser = userService.getAuthenticatedUser();
        Order newOrder = orderService.createOrder(orderMapper.toEntity(order), currentUser);
        return new ResponseDto<>(HttpStatus.CREATED, orderMapper.toDto(newOrder));
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get orders for the current user",
            description = "Retrieves a paginated list of orders for the current authenticated user"
    )
    public ResponseDto<PagedDto<OrderDto>> getOrder(@Valid @ModelAttribute PaginationParams paginationParams) {
        User currentUser = userService.getAuthenticatedUser();
        Page<Order> orders = orderService.findAllByUserId(currentUser.getId(), paginationParams);
        Page<OrderDto> orderDtos = orders.map(orderMapper::toDto);
        PagedDto<OrderDto> pagedDto = new PagedDto<>(orderDtos);
        return new ResponseDto<>(HttpStatus.OK, pagedDto);
    }
}

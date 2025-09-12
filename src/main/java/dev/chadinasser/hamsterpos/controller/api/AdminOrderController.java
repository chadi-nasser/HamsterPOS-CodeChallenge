package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.*;
import dev.chadinasser.hamsterpos.mapper.OrderMapper;
import dev.chadinasser.hamsterpos.mapper.ProductMapper;
import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.model.Product;
import dev.chadinasser.hamsterpos.service.OrderService;
import dev.chadinasser.hamsterpos.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(
        name = "Admin Order Management",
        description = "Endpoints for admin to manage orders and view low stock products"
)
public class AdminOrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    AdminOrderController(OrderService orderService, ProductService productService, OrderMapper orderMapper, ProductMapper productMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderMapper = orderMapper;
        this.productMapper = productMapper;
    }

    @GetMapping("/orders")
    @Operation(summary = "List all orders", description = "Admin-only endpoint to retrieve all orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<PagedDto<OrderDto>> getAllOrders(@Valid @ModelAttribute PaginationParams paginationParams) {
        Page<Order> orders = orderService.findAll(paginationParams);
        Page<OrderDto> orderDtos = orders.map(orderMapper::toDto);
        PagedDto<OrderDto> pagedDto = new PagedDto<>(orderDtos);
        return new ResponseDto<>(HttpStatus.OK, pagedDto);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "List low stock products", description = "Admin-only endpoint to retrieve products with low stock (less than 5)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<PagedDto<ProductDto>> getLowStockProducts(@Valid @ModelAttribute PaginationParams paginationParams, @RequestParam(defaultValue = "5") Integer threshold) {
        Page<Product> products = productService.findAllWithStockLessThan(threshold, paginationParams);
        Page<ProductDto> productDtos = products.map(productMapper::toDto);
        PagedDto<ProductDto> pagedDto = new PagedDto<>(productDtos);
        return new ResponseDto<>(HttpStatus.OK, pagedDto);
    }
}
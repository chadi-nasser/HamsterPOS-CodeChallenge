package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.OrderDto;
import dev.chadinasser.hamsterpos.dto.ProductDto;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.mapper.OrderMapper;
import dev.chadinasser.hamsterpos.mapper.ProductMapper;
import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.model.Product;
import dev.chadinasser.hamsterpos.service.OrderService;
import dev.chadinasser.hamsterpos.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<List<OrderDto>> getAllOrders() {
        List<Order> orders = orderService.findAll();
        List<OrderDto> orderDtos = orders.stream().map(orderMapper::toDto).toList();
        return new ResponseDto<>(HttpStatus.OK, orderDtos);
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<List<ProductDto>> getLowStockProducts() {
        List<Product> products = productService.findAllWithStockLessThan(10);
        List<ProductDto> productDtos = products.stream().map(productMapper::toDto).toList();
        return new ResponseDto<>(HttpStatus.OK, productDtos);
    }
}
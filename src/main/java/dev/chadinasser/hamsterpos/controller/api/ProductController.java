package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.ProductDto;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.mapper.ProductMapper;
import dev.chadinasser.hamsterpos.model.Product;
import dev.chadinasser.hamsterpos.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(
        name = "Product Management",
        description = "Endpoints for managing products"
)
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    @Operation(
            summary = "Get All Products",
            description = "Retrieve a list of all products"
    )
    public ResponseDto<List<ProductDto>> getProducts() {
        return new ResponseDto<>(HttpStatus.OK, productService.findAll().stream().map(productMapper::toDto).toList());
    }

    @PostMapping
    @Operation(
            summary = "Add New Product",
            description = "Add a new product to the inventory (Admin only)"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<ProductDto> createProduct(@Valid @RequestBody ProductDto product) {
        Product newProduct = productService.createProduct(productMapper.toEntity(product));
        return new ResponseDto<>(HttpStatus.CREATED, productMapper.toDto(newProduct));
    }
}

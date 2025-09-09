package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.ProductDto;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(
            summary = "Get All Products",
            description = "Retrieve a list of all products"
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseDto<List<ProductDto>> getProducts() {
        return new ResponseDto<>(HttpStatus.OK, productService.findAll().stream().map(new ProductDto()::fromEntity).toList());
    }

    @PostMapping
    @Operation(
            summary = "Add New Product",
            description = "Add a new product to the inventory (Admin only)"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<ProductDto> createProduct(@Valid @RequestBody ProductDto product) {
        Product newProduct = productService.createProduct(product.toEntity());
        return new ResponseDto<>(HttpStatus.CREATED, new ProductDto().fromEntity(newProduct));
    }
}

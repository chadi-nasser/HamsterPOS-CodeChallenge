package dev.chadinasser.hamsterpos.controller.api;

import dev.chadinasser.hamsterpos.dto.PagedDto;
import dev.chadinasser.hamsterpos.dto.PaginationParams;
import dev.chadinasser.hamsterpos.dto.ProductDto;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.mapper.ProductMapper;
import dev.chadinasser.hamsterpos.model.Product;
import dev.chadinasser.hamsterpos.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

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
            description = "Retrieve a list of products with optional filtering by name, price range, and stock"
    )
    public ResponseDto<PagedDto<ProductDto>> getProducts(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false, defaultValue = "false") Boolean inStockOnly,
            @Valid @ModelAttribute PaginationParams paginationParams) {

        Page<Product> products = productService.findAllWithFilters(name, minPrice, maxPrice, minStock, inStockOnly, paginationParams);
        Page<ProductDto> productDtos = products.map(productMapper::toDto);
        PagedDto<ProductDto> pagedDto = new PagedDto<>(productDtos);
        return new ResponseDto<>(HttpStatus.OK, pagedDto);
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

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Product by ID",
            description = "Retrieve a specific product by its ID"
    )
    public ResponseDto<ProductDto> getProductById(@PathVariable UUID id) {
        Product product = productService.findById(id);
        return new ResponseDto<>(HttpStatus.OK, productMapper.toDto(product));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Product",
            description = "Update an existing product (Admin only)"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<ProductDto> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductDto productDto) {
        Product updatedProduct = productService.updateProduct(id, productMapper.toEntity(productDto));
        return new ResponseDto<>(HttpStatus.OK, productMapper.toDto(updatedProduct));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Product",
            description = "Delete a product (Admin only)"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDto<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return new ResponseDto<>(HttpStatus.NO_CONTENT, null);
    }
}

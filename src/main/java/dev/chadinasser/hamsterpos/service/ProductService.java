package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.dto.PaginationParams;
import dev.chadinasser.hamsterpos.exception.InsufficientStockException;
import dev.chadinasser.hamsterpos.exception.ResourceAlreadyExistException;
import dev.chadinasser.hamsterpos.exception.ResourceNotFoundException;
import dev.chadinasser.hamsterpos.model.Product;
import dev.chadinasser.hamsterpos.repo.ProductRepo;
import dev.chadinasser.hamsterpos.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product findById(UUID id) {
        return productRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id)
        );
    }

    public Product createProduct(Product product) {
        productRepo.findByName(product.getName()).ifPresent(
                p -> {
                    throw new ResourceAlreadyExistException("Product", "name", product.getName());
                }
        );
        return productRepo.save(product);
    }

    @Transactional
    public Product updateProduct(UUID id, Product productUpdate) {
        Product existingProduct = findById(id);

        // Check if name is being changed and if new name already exists
        if (!existingProduct.getName().equals(productUpdate.getName())) {
            productRepo.findByName(productUpdate.getName()).ifPresent(
                    p -> {
                        throw new ResourceAlreadyExistException("Product", "name", productUpdate.getName());
                    }
            );
        }

        existingProduct.setName(productUpdate.getName() != null ? productUpdate.getName() : existingProduct.getName());
        existingProduct.setPrice(productUpdate.getPrice() != null ? productUpdate.getPrice() : existingProduct.getPrice());
        existingProduct.setStock(productUpdate.getStock() != null ? productUpdate.getStock() : existingProduct.getStock());

        return productRepo.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        Product product = findById(id);
        productRepo.delete(product);
    }

    @Transactional
    public void decreaseStock(UUID productId, Integer quantity) {
        Product product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
        if (product.getStock() < quantity) {
            throw new InsufficientStockException(product.getName(), product.getStock(), quantity);
        }
        product.setStock(product.getStock() - quantity);
        productRepo.save(product);
    }

    public Page<Product> findAllWithStockLessThan(Integer threshold, PaginationParams paginationParams) {
        return productRepo.findAllByStockLessThan(threshold, paginationParams.toPageable());
    }


    public Page<Product> findAllWithFilters(String search, BigDecimal minPrice, BigDecimal maxPrice,
                                            Integer minStock, Boolean inStockOnly, PaginationParams paginationParams) {
        Specification<Product> spec = ProductSpecification.likeName(search)
                .and(ProductSpecification.priceGreaterThanOrEqual(minPrice))
                .and(ProductSpecification.priceLessThanOrEqual(maxPrice))
                .and(ProductSpecification.stockGreaterThanOrEqual(minStock));

        if (Boolean.TRUE.equals(inStockOnly)) {
            spec = spec.and(ProductSpecification.inStock());
        }

        return productRepo.findAll(spec, paginationParams.toPageable());
    }
}

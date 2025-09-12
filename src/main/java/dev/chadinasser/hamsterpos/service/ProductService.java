package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.exception.ResourceAlreadyExistException;
import dev.chadinasser.hamsterpos.exception.ResourceNotFoundException;
import dev.chadinasser.hamsterpos.model.Product;
import dev.chadinasser.hamsterpos.repo.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> findAll() {
        return productRepo.findAll();
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
    public void decreaseStock(UUID productId, int quantity) {
        Product product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }
        product.setStock(product.getStock() - quantity);
        productRepo.save(product);
    }

    public List<Product> findAllWithStockLessThan(int stock) {
        return productRepo.findAllByStockLessThan(stock);
    }
}

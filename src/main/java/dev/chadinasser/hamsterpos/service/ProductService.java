package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.model.Product;
import dev.chadinasser.hamsterpos.repo.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public Product addProduct(Product product) {
        return null;
    }
}

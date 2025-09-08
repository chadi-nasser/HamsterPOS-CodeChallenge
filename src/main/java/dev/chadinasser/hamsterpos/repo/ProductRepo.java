package dev.chadinasser.hamsterpos.repo;

import dev.chadinasser.hamsterpos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
}

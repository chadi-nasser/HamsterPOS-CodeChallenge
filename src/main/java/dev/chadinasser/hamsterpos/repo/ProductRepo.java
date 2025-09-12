package dev.chadinasser.hamsterpos.repo;

import dev.chadinasser.hamsterpos.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {
    Optional<Product> findByName(String name);

    Page<Product> findAllByStockLessThan(Integer threshold, Pageable pageable);
}

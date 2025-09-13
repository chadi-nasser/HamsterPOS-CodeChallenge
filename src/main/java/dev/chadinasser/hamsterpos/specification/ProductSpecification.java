package dev.chadinasser.hamsterpos.specification;

import dev.chadinasser.hamsterpos.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> likeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + name.toLowerCase().trim() + "%"
        );
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        if (minPrice == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                root.get("price"), minPrice
        );
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                root.get("price"), maxPrice
        );
    }

    public static Specification<Product> stockGreaterThanOrEqual(Integer minStock) {
        if (minStock == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                root.get("stock"), minStock
        );
    }

    public static Specification<Product> inStock() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(
                root.get("stock"), 0
        );
    }
}

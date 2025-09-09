package dev.chadinasser.hamsterpos.dto;

import dev.chadinasser.hamsterpos.model.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDto implements EntityMapper<Product, ProductDto> {
    @NotNull
    @Size(min = 1, max = 100, message = "Product name must be between 1 and 100 characters")
    private String name;

    @PositiveOrZero(message = "Price must be non-negative")
    private Double price;
    @PositiveOrZero(message = "Stock must be non-negative")
    private Integer stock;

    @Override
    public Product toEntity() {
        Product product = new Product();
        product.setName(this.name);
        product.setPrice(this.price);
        product.setStock(this.stock);
        return product;
    }

    @Override
    public ProductDto fromEntity(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        return this;
    }
}

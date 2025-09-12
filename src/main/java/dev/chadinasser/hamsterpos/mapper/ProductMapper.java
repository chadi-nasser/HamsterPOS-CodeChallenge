package dev.chadinasser.hamsterpos.mapper;

import dev.chadinasser.hamsterpos.dto.ProductDto;
import dev.chadinasser.hamsterpos.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements EntityMapper<Product, ProductDto> {
    @Override
    public Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        return product;
    }

    @Override
    public ProductDto toDto(Product entity) {
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        return dto;
    }
}

package dev.chadinasser.hamsterpos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductDto {
    private UUID id;

    @NotNull
    @Size(min = 1, max = 100, message = "Product name must be between 1 and 100 characters")
    private String name;

    @PositiveOrZero(message = "Price must be non-negative")
    private Double price;
    @PositiveOrZero(message = "Stock must be non-negative")
    private Integer stock;
}

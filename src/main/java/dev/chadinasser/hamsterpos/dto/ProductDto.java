package dev.chadinasser.hamsterpos.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private Double price;
    private Integer stock;
}

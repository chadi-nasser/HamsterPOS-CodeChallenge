package dev.chadinasser.hamsterpos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.chadinasser.hamsterpos.model.OrderStatus;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
    private UUID orderId;
    private OrderStatus orderStatus;
    private Double totalAmount;
    private List<ProductDto> products;
}

package dev.chadinasser.hamsterpos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.chadinasser.hamsterpos.model.Order.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalAmount;

    @NotNull
    @Size(min = 1, message = "At least one item is required")
    @Valid
    private List<OrderItemDto> items;
}
package dev.chadinasser.hamsterpos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.model.OrderItem;
import dev.chadinasser.hamsterpos.model.OrderStatus;
import dev.chadinasser.hamsterpos.model.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto implements EntityMapper<Order, OrderDto> {
    private UUID id;

    @NotNull
    private OrderStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double totalPrice;

    @NotNull
    @Size(min = 1, message = "At least one item is required")
    @Valid
    private List<OrderItemDto> items;

    @Override
    public Order toEntity() {
        Order order = new Order();
        order.setId(this.id);
        order.setStatus(this.status);

        if (this.items != null) {
            List<OrderItem> entityItems = new ArrayList<>(this.items.size());
            for (OrderItemDto i : this.items) {
                OrderItem oi = new OrderItem();
                Product p = new Product();
                p.setId(i.getProductId());
                oi.setProduct(p);
                oi.setQuantity(i.getQuantity());
                oi.setUnitPrice(i.getUnitPrice());
                oi.setOrder(order);
                entityItems.add(oi);
            }
            order.setItems(entityItems);
        }

        return order;
    }

    @Override
    public OrderDto fromEntity(Order entity) {
        this.id = entity.getId();
        this.status = entity.getStatus();
        this.totalPrice = entity.getTotalPrice();

        if (entity.getItems() != null) {
            List<OrderItemDto> dtoItems = new ArrayList<>(entity.getItems().size());
            for (OrderItem oi : entity.getItems()) {
                OrderItemDto dto = new OrderItemDto();
                dto.setProductId(oi.getProduct() != null ? oi.getProduct().getId() : null);
                dto.setQuantity(oi.getQuantity());
                dto.setUnitPrice(oi.getUnitPrice());
                dtoItems.add(dto);
            }
            this.items = dtoItems;
        } else {
            this.items = List.of();
        }

        return this;
    }
}
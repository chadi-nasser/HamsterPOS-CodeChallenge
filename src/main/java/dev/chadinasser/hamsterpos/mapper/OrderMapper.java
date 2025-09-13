package dev.chadinasser.hamsterpos.mapper;

import dev.chadinasser.hamsterpos.dto.OrderDto;
import dev.chadinasser.hamsterpos.dto.OrderItemDto;
import dev.chadinasser.hamsterpos.model.Order;
import dev.chadinasser.hamsterpos.model.OrderItem;
import dev.chadinasser.hamsterpos.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper implements EntityMapper<Order, OrderDto> {
    @Override
    public Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());

        if (dto.getItems() != null) {
            List<OrderItem> entityItems = new ArrayList<>(dto.getItems().size());
            for (OrderItemDto i : dto.getItems()) {
                OrderItem oi = new OrderItem();
                Product p = new Product();
                p.setId(i.getProductId());
                oi.setProduct(p);
                oi.setQuantity(i.getQuantity());
                oi.setOrder(order);
                entityItems.add(oi);
            }
            order.setItems(entityItems);
        }

        return order;
    }

    @Override
    public OrderDto toDto(Order entity) {
        OrderDto dto = new OrderDto();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setTotalAmount(entity.getTotalAmount());

        if (entity.getItems() != null) {
            List<OrderItemDto> dtoItems = new ArrayList<>(entity.getItems().size());
            for (OrderItem oi : entity.getItems()) {
                OrderItemDto itemDto = new OrderItemDto();
                itemDto.setProductId(oi.getProduct() != null ? oi.getProduct().getId() : null);
                itemDto.setQuantity(oi.getQuantity());
                itemDto.setUnitPrice(oi.getUnitPrice());
                dtoItems.add(itemDto);
            }
            dto.setItems(dtoItems);
        } else {
            dto.setItems(List.of());
        }

        return dto;
    }
}

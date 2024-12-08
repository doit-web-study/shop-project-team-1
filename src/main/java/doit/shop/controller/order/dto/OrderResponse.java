package doit.shop.controller.order.dto;

import doit.shop.domain.Order;

import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        LocalDateTime createdAt,
        Long totalPrice,
        Integer quantity,
        String status,
        Long productId,
        String productName
) {
    public static OrderResponse fromEntity(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getTotalPrice(),
                order.getQuantity(),
                order.getStatus(),
                order.getProduct().getId(),
                order.getProduct().getName()
        );
    }
}

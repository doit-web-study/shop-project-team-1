package doit.shop.controller.order.dto;

import doit.shop.controller.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponse {

        Long orderId;
        LocalDateTime orderCreatedAt;
        Integer orderTotalPrice;
        Integer numberOfProduct;
        OrderStatus orderStatus;
}

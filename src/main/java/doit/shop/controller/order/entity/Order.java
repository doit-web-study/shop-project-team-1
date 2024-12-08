package doit.shop.controller.order.entity;

import doit.shop.controller.product.entity.Product;
import doit.shop.controller.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer orderedStock;
    private OrderStatus orderStatus;
    private Integer totalPrice;

    @ManyToOne
    Product product;
    @ManyToOne
    UserEntity user;

    @Builder
    public Order(Integer orderedStock, OrderStatus orderStatus, Integer totalPrice, Product product, UserEntity user) {
        this.createdAt = LocalDateTime.now();
        this.orderedStock = orderedStock;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.product = product;
        this.user = user;
    }

    public void changeStatus(OrderStatus status){
        this.orderStatus = status;
    }
}

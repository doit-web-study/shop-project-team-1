package doit.shop.controller.order.dto;

import lombok.Getter;

@Getter
public class OrderProductRequest {

    private Long productId;
    private Integer numberOfProducts;
}

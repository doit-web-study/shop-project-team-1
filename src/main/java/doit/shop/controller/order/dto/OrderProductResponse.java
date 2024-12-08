package doit.shop.controller.order.dto;

import doit.shop.controller.product.dto.ProductInfoResponse;
import lombok.Getter;

@Getter
public class OrderProductResponse {

    private OrderResponse orderResponse;
    private ProductInfoResponse productInfoResponse;
}

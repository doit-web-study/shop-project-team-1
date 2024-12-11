package doit.shop.controller.order.dto;

import doit.shop.controller.product.dto.ProductInfoResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderProductResponse {

    private OrderResponse orderResponse;
    private ProductInfoResponse productInfoResponse;
}

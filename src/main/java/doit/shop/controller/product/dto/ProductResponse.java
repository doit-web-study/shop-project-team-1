package doit.shop.controller.product.dto;

import lombok.Getter;

@Getter
public class ProductResponse {

    private Long productId;

    public ProductResponse(Long productId) {
        this.productId = productId;
    }
}

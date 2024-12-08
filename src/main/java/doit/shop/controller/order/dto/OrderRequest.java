package doit.shop.controller.order.dto;

public record OrderRequest(
        Long productId,
        Integer numberOfProduct
) {
}

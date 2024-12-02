package doit.shop.controller.product.dto;

public record ProductRequest(
        String productName,
        String productDescription,
        Integer productPrice,
        Integer productStock,
        String productImage,
        Long categoryId
) {
}

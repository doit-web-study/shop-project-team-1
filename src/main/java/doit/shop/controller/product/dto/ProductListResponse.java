package doit.shop.controller.product.dto;

import java.time.LocalDateTime;

public record ProductListResponse(
        Long productId,
        String productName,
        String productDescription,
        Integer productPrice,
        Integer productStock,
        String productImage,
        LocalDateTime productCreatedAt,
        LocalDateTime productModifiedAt,
        Long categoryId,
        String categoryType
) {
}
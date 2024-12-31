package doit.shop.domain.product.dto;

import doit.shop.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ProductUploadResponse(
        @Schema(description = "상품 식별 ID", example = "1")
        Long productId
) {
    public static ProductUploadResponse from(Product product) {
        return ProductUploadResponse.builder()
                .productId(product.getId())
                .build();
    }
}

package doit.shop.domain.product.dto;

import doit.shop.domain.product.entity.Product;
import doit.shop.domain.user.dto.UserInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductInfoResponse(
        @Schema(description = "상품 이름", example = "사과")
        String productName,

        @Schema(description = "상품 상세 설명", example = "달달하고 죄송한")
        String productDescription,

        @Schema(description = "상품 가격", example = "30000")
        Integer productPrice,

        @Schema(description = "상품 재고", example = "20")
        Integer productStock,

        @Schema(description = "상품 사진", example = "dkskld...skjdf")
        String productImage,

        @Schema(description = "상품 등록 시간", example = "2024-12-03T10:45:00")
        LocalDateTime productCreatedAt,

        @Schema(description = "상품 수정 시간", example = "2024-12-03T10:45:00")
        LocalDateTime productModifiedAt,

        @Schema(description = "상품 카테고리", example = "1234")
        Long categoryId
) {
        public static ProductInfoResponse from(Product product) {
                return ProductInfoResponse.builder()
                        .productName(product.getName())
                        .productDescription(product.getDescription())
                        .productPrice(product.getPrice())
                        .productStock(product.getStock())
                        .productImage(product.getImage())
                        .productCreatedAt(product.getProductCreatedAt())
                        .productModifiedAt(product.getProductModifiedAt())
                        .categoryId(product.getCategoryId())
                        .build();
        }
}

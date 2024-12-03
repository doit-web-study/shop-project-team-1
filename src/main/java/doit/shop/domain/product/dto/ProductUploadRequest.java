package doit.shop.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductUploadRequest(
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

        @Schema(description = "상품 카테고리", example = "1234")
        Long categoryId
) {

}

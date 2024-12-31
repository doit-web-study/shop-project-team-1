package doit.shop.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponse(
        @Schema(description = "카테고리 id", example = "1")
        Long categoryId,

        @Schema(description = "카테고리 타입", example = "식품")
        String categoryType
) {

}

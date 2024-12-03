package doit.shop.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product", description = "상품 관련 API")
interface ProductController {

    @Operation(summary = "상품 조회", description = "상품을 조회한다.")
    @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    @ApiResponse(responseCode = "400", description = "상품 조회 실패")
    void upload(
            @Schema(description = "상품 이름", example = "사과")
            String productName

            @Schema(description = "상품 상세 설명", example = "달달하고 맛있는..")
		"productDescription" : String,
		"productPrice" : Integer,
		"productStock" : Integer,
		"productImage" : String || null,
		"categoryId" : Long
    )

    @Operation(summary = "상품 등록", description = "상품을 등록한다.")
    @ApiResponse(responseCode = "200", description = "상품 등록 성공")
    @ApiResponse(responseCode = "400", description = "상품 등록 실패")
    void

}

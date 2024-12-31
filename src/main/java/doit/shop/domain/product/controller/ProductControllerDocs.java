package doit.shop.domain.product.controller;

import doit.shop.domain.product.dto.ProductInfoResponse;
import doit.shop.domain.product.dto.ProductUploadRequest;
import doit.shop.domain.product.dto.ProductUploadResponse;
import doit.shop.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Product", description = "상품 관련 API")
interface ProductControllerDocs {

    @Operation(summary = "상품 1개 조회", description = "지정된 1개의 상품을 조회한다.")
    @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    @ApiResponse(responseCode = "400", description = "상품 조회 실패")
    ResponseEntity<ProductInfoResponse> searchOne(
        @Schema(description = "상품 아이디", example = "1234")
        Long id
    );

    @Operation(summary = "상품 등록", description = "상품을 등록한다.")
    @ApiResponse(responseCode = "200", description = "상품 등록 성공")
    @ApiResponse(responseCode = "400", description = "상품 등록 실패")
    ResponseEntity<ProductUploadResponse> upload(
            @Schema(description = "상품 정보", implementation = ProductUploadRequest.class)
            ProductUploadRequest productUploadRequest
    );

    @Operation(summary = "상품 여러 개 조회", description = "정렬 기준에 따라 상품을 여러 개 조회한다.")
    @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    @ApiResponse(responseCode = "400", description = "상품 조회 실패")
    void searchMany(
            @Schema(description = "페이징 현재 번호", example = "1")
            Long pageId,

            @Schema(description = "상품 정렬 기준", example = "최신순")
            String orderBy
    );

}

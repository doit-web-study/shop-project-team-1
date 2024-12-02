package doit.shop.controller.product;

import doit.shop.controller.product.dto.ProductListResponse;

import java.util.List;

public record PagedProductResponse(
        List<ProductListResponse> result,
        int pageNumber,
        boolean hasPrevious,
        boolean hasNext
) {
}

package doit.shop.controller.product.dto;

import java.util.List;

public record PagedProductResponse(
        List<ProductListResponse> result,
        int pageNumber,
        boolean hasPrevious,
        boolean hasNext
) {
}
package doit.shop.port.product;

import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductListResponse;

import java.util.List;

public interface ProductPersistencePort {
    // 페이징된 제품 조회
    PagedProductResponse getPagedProducts(int pageNumber, String keyword, Long categoryId, String orderBy);

    // 페이징 없이 모든 제품 조회
    List<ProductListResponse> getAllProducts(String keyword, Long categoryId);
}

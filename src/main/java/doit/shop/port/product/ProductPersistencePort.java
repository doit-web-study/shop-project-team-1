package doit.shop.port.product;

import doit.shop.controller.product.dto.PagedProductResponse;

public interface ProductPersistencePort {
    PagedProductResponse getPagedProducts(int pageNumber, String keyword, Long categoryId, String orderBy);
}

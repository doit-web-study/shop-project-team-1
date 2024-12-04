package doit.shop.controller.product.repository;

import doit.shop.controller.product.dto.ProductInfoResponse;
import doit.shop.controller.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<ProductInfoResponse> searchKeyword(Pageable pageable, String keyword);
    Page<ProductInfoResponse> searchKeywordByCategoryId(Pageable pageable, String keyword, Long categoryId);

}

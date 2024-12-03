package doit.shop.controller.product.repository;

import doit.shop.controller.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> searchKeyword(Pageable pageable, String keyword);
    Page<Product> searchKeywordByCategoryId(Pageable pageable, String keyword, Long categoryId);

}

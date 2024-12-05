package doit.shop.controller.product.repository;

import doit.shop.controller.product.entity.Category;
import doit.shop.controller.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    Product findByProductId(Long productId);
    Product findByCategory(Category category);
}

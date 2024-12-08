package doit.shop.service;

import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductRequest;
import doit.shop.controller.product.dto.ProductResponse;
import doit.shop.domain.Category;
import doit.shop.domain.Product;

import doit.shop.port.product.ProductPersistencePort;
import doit.shop.repository.CategoryRepository;
import doit.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPersistencePort productPersistencePort;

    public ProductResponse registerProduct(ProductRequest productRequest, String productImage) {
        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        String imageUrl = null;
        if (productImage != null && !productImage.isEmpty()) {
            imageUrl = productImage;
        }

        Product product = Product.builder()
                .name(productRequest.productName())
                .description(productRequest.productDescription())
                .image(imageUrl)
                .price(productRequest.productPrice())
                .stock(productRequest.productStock())
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);
        return new ProductResponse(savedProduct.getId());
    }

    public PagedProductResponse getProducts(int pageNumber, String keyword, Long categoryId, String orderBy) {
        return productPersistencePort.getPagedProducts(pageNumber, keyword, categoryId, orderBy);
    }
}

package doit.shop.domain.product.service;

import doit.shop.domain.product.dto.ProductInfoResponse;
import doit.shop.domain.product.dto.ProductUploadRequest;
import doit.shop.domain.product.dto.ProductUploadResponse;
import doit.shop.domain.product.entity.Product;
import doit.shop.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductUploadResponse upload(ProductUploadRequest productUploadRequest) {
        Product product = Product.builder()
                .name(productUploadRequest.productName())
                .price(productUploadRequest.productPrice())
                .description(productUploadRequest.productDescription())
                .image(productUploadRequest.productImage())
                .stock(productUploadRequest.productStock())
                .categoryId(productUploadRequest.categoryId())
                .build();

        Product savedProduct = productRepository.save(product);
        return ProductUploadResponse.from(savedProduct);
    }

    public ProductInfoResponse searchOne(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }

        return ProductInfoResponse.from(product);
    }
}

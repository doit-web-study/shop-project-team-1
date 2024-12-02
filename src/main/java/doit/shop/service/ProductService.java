package doit.shop.service;

import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductListResponse;
import doit.shop.controller.product.dto.ProductRequest;
import doit.shop.controller.product.dto.ProductResponse;
import doit.shop.domain.Category;
import doit.shop.domain.Product;
import doit.shop.domain.User;
import doit.shop.repository.CategoryRepository;
import doit.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse registerProduct(ProductRequest productRequest, String productImage) {
        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        System.out.println(productRequest);
        System.out.println(productImage);
        String imageUrl = null;
        if (productImage != null && !productImage.isEmpty()) {
            imageUrl = productImage; // Placeholder for image saving logic
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

    public PagedProductResponse getProducts(int pageNumber, String keyword, Long categoryId) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Product> productPage = categoryId != null
                ? productRepository.findByCategoryId(categoryId, pageRequest)
                : productRepository.findAll(pageRequest);

        List<ProductListResponse> products = productPage.stream()
                .map(product -> new ProductListResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.getImage(),
                        product.getCreatedAt(),
                        product.getUpdatedAt(),
                        product.getCategory().getId(),
                        product.getCategory().getCategoryType().name()

                ))
                .collect(Collectors.toList());

        return new PagedProductResponse(
                products,
                productPage.getNumber(),
                productPage.hasPrevious(),
                productPage.hasNext()
        );
    }
}

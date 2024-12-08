package doit.shop.port.product;

import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductListResponse;
import doit.shop.domain.Product;
import doit.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Primary
public class ProductJpaAdapter implements ProductPersistencePort {
    // Spring Data JPA를 사용하여 HQL 기반으로 쿼리를 작성
    private final ProductRepository productRepository;

    @Override
    public PagedProductResponse getPagedProducts(int pageNumber, String keyword, Long categoryId, String orderBy) {
        // 정렬 기준 설정
        Sort sort = "views".equalsIgnoreCase(orderBy)
                ? Sort.by(Sort.Direction.DESC, "views")
                : Sort.by(Sort.Direction.DESC, "createdAt");

        // 페이징 설정
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, sort);

        // 검색 및 필터링
        Page<Product> productPage = categoryId != null
                ? productRepository.findByCategoryIdAndKeyword(categoryId, keyword, pageRequest)
                : productRepository.findByKeyword(keyword, pageRequest);

        // DTO로 변환
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

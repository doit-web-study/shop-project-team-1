package doit.shop.port.product;

import doit.shop.controller.product.dto.ProductListResponse;
import doit.shop.domain.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class ProductAdapterUtil {

    // 정렬 및 페이징 설정
    public static PageRequest createPageRequest(int pageNumber, String orderBy) {
        Sort sort = "views".equalsIgnoreCase(orderBy)
                ? Sort.by(Sort.Direction.DESC, "views")
                : Sort.by(Sort.Direction.DESC, "createdAt");
        return PageRequest.of(pageNumber, 10, sort);
    }

    // Product 엔티티를 DTO로 변환
    public static List<ProductListResponse> toProductListResponse(List<Product> products) {
        return products.stream()
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
    }
}
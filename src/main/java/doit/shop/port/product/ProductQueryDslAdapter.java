package doit.shop.port.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductListResponse;
import doit.shop.domain.Product;
import doit.shop.domain.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductQueryDslAdapter implements ProductPersistencePort {

    // QueryDSL을 사용해 타입 안전하고 동적으로 쿼리를 생성
    private final JPAQueryFactory queryFactory;

    @Override
    public PagedProductResponse getPagedProducts(int pageNumber, String keyword, Long categoryId, String orderBy) {
        QProduct product = QProduct.product;

        // 정렬 기준 설정
        Sort sort = "views".equalsIgnoreCase(orderBy)
                ? Sort.by(Sort.Direction.DESC, "views")
                : Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, sort);

        // 동적 필터 생성
        BooleanBuilder builder = new BooleanBuilder();
        if (categoryId != null) {
            builder.and(product.category.id.eq(categoryId));
        }
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(product.name.containsIgnoreCase(keyword));
        }

        // QueryDSL로 데이터 조회
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(builder)
                .orderBy(product.createdAt.desc()) // 기본 정렬
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        // DTO 변환
        List<ProductListResponse> productResponses = products.stream()
                .map(p -> new ProductListResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock(),
                        p.getImage(),
                        p.getCreatedAt(),
                        p.getUpdatedAt(),
                        p.getCategory().getId(),
                        p.getCategory().getCategoryType().name()
                ))
                .collect(Collectors.toList());

        return new PagedProductResponse(
                productResponses,
                pageRequest.getPageNumber(),
                pageRequest.getOffset() > 0, // 이전 페이지 존재 여부
                productResponses.size() == pageRequest.getPageSize() // 다음 페이지 존재 여부
        );
    }
}

package doit.shop.port.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductListResponse;
import doit.shop.domain.Product;
import doit.shop.domain.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Primary
public class ProductQueryDslAdapter implements ProductPersistencePort {

    // QueryDSL을 사용해 타입 안전하고 동적으로 쿼리를 생성
    private final JPAQueryFactory queryFactory;

    @Override
    public PagedProductResponse getPagedProducts(int pageNumber, String keyword, Long categoryId, String orderBy) {
        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();
        if (categoryId != null) {
            builder.and(product.category.id.eq(categoryId));
        }
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(product.name.containsIgnoreCase(keyword));
        }

        PageRequest pageRequest = ProductAdapterUtil.createPageRequest(pageNumber, orderBy);

        List<Product> products = queryFactory
                .selectFrom(product)
                .where(builder)
                .orderBy(product.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        List<ProductListResponse> productResponses = ProductAdapterUtil.toProductListResponse(products);

        return new PagedProductResponse(
                productResponses,
                pageRequest.getPageNumber(),
                pageRequest.getOffset() > 0,
                productResponses.size() == pageRequest.getPageSize()
        );
    }

    @Override
    public List<ProductListResponse> getAllProducts(String keyword, Long categoryId) {
        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();
        if (categoryId != null) {
            builder.and(product.category.id.eq(categoryId));
        }
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(product.name.containsIgnoreCase(keyword));
        }

        List<Product> products = queryFactory
                .selectFrom(product)
                .where(builder)
                .fetch();

        return ProductAdapterUtil.toProductListResponse(products);
    }
}

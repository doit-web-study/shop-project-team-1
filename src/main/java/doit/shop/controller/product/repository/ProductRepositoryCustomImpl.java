package doit.shop.controller.product.repository;

import doit.shop.controller.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static doit.shop.controller.product.entity.QProduct.product;

@RequiredArgsConstructor
@Component
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchKeyword(Pageable pageable, String keyword) {

        List<Product> products = queryFactory
                .selectFrom(product)
                .where(product.description.contains(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.modifiedAt.asc())
                .fetch();

        Long total = queryFactory.select(product.count())
                .from(product)
                .where(product.description.contains(keyword))
                .fetchOne();

        return new PageImpl<>(products, pageable, total);
    }

    @Override
    public Page<Product> searchKeywordByCategoryId(Pageable pageable, String keyword, Long categoryId) {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(product.description.contains(keyword), product.categoryId.eq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.modifiedAt.asc())
                .fetch();

        Long total = queryFactory.select(product.count())
                .from(product)
                .where(product.description.contains(keyword), product.categoryId.eq(categoryId))
                .fetchOne();

        return new PageImpl<>(products, pageable, total);
    }
}

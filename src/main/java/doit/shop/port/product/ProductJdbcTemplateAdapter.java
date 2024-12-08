package doit.shop.port.product;

import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductListResponse;
import doit.shop.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor

public class ProductJdbcTemplateAdapter implements ProductPersistencePort {
    // SQL 문자열을 직접 작성하여 데이터베이스와 상호작용

    private final JdbcTemplate jdbcTemplate;

    @Override
    public PagedProductResponse getPagedProducts(int pageNumber, String keyword, Long categoryId, String orderBy) {
        String baseQuery = "SELECT * FROM product p WHERE 1=1 ";
        StringBuilder whereClause = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (categoryId != null) {
            whereClause.append(" AND p.category_id = ? ");
            params.add(categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            whereClause.append(" AND LOWER(p.name) LIKE LOWER(?) ");
            params.add("%" + keyword + "%");
        }

        Sort sort = "views".equalsIgnoreCase(orderBy)
                ? Sort.by(Sort.Direction.DESC, "views")
                : Sort.by(Sort.Direction.DESC, "createdAt");
        String orderAndPagination = " ORDER BY " + sort.iterator().next().getProperty() + " DESC LIMIT ? OFFSET ? ";
        params.add(10);
        params.add(pageNumber * 10);

        List<Product> products = jdbcTemplate.query(
                baseQuery + whereClause + orderAndPagination,
                params.toArray(),
                new BeanPropertyRowMapper<>(Product.class)
        );

        List<ProductListResponse> productResponses = ProductAdapterUtil.toProductListResponse(products);

        return new PagedProductResponse(
                productResponses,
                pageNumber,
                pageNumber > 0,
                productResponses.size() == 10
        );
    }

    @Override
    public List<ProductListResponse> getAllProducts(String keyword, Long categoryId) {
        String baseQuery = "SELECT * FROM product p WHERE 1=1 ";
        StringBuilder whereClause = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (categoryId != null) {
            whereClause.append(" AND p.category_id = ? ");
            params.add(categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            whereClause.append(" AND LOWER(p.name) LIKE LOWER(?) ");
            params.add("%" + keyword + "%");
        }

        List<Product> products = jdbcTemplate.query(
                baseQuery + whereClause,
                params.toArray(),
                new BeanPropertyRowMapper<>(Product.class)
        );

        return ProductAdapterUtil.toProductListResponse(products);
    }
}

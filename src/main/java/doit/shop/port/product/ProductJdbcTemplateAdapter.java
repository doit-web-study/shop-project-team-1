package doit.shop.port.product;

import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductListResponse;
import doit.shop.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductJdbcTemplateAdapter implements ProductPersistencePort {
    // SQL 문자열을 직접 작성하여 데이터베이스와 상호작용

    private final JdbcTemplate jdbcTemplate;

    @Override
    public PagedProductResponse getPagedProducts(int pageNumber,
                                                 String keyword,
                                                 Long categoryId,
                                                 String orderBy)
    {
        String baseQuery = "SELECT * FROM product p WHERE 1=1 ";
        String countQuery = "SELECT COUNT(*) FROM product p WHERE 1=1 ";
        StringBuilder whereClause = new StringBuilder();

        // 필터링 조건 추가
        List<Object> params = new ArrayList<>();
        if (categoryId != null) {
            whereClause.append(" AND p.category_id = ? ");
            params.add(categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            whereClause.append(" AND LOWER(p.name) LIKE LOWER(?) ");
            params.add("%" + keyword + "%");
        }

        // 정렬 및 페이징
        Sort sort = "views".equalsIgnoreCase(orderBy)
                ? Sort.by(Sort.Direction.DESC, "views")
                : Sort.by(Sort.Direction.DESC, "createdAt");
        String orderAndPagination = " ORDER BY " + sort.iterator().next().getProperty() + " DESC LIMIT ? OFFSET ? ";
        params.add(10); // page size
        params.add(pageNumber * 10); // offset

        // 데이터 조회
        List<Product> products = jdbcTemplate.query(
                baseQuery + whereClause + orderAndPagination,
                params.toArray(),
                new BeanPropertyRowMapper<>(Product.class)
        );

        // 총 데이터 수
        long total = jdbcTemplate.queryForObject(
                countQuery + whereClause,
                params.toArray(new Object[params.size() - 2]),
                Long.class
        );

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
                pageNumber,
                pageNumber > 0,
                productResponses.size() == 10
        );
    }
}

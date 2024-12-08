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
public class ProductJpaAdapter implements ProductPersistencePort {
    // Spring Data JPA를 사용하여 HQL 기반으로 쿼리를 작성
    private final ProductRepository productRepository;

    @Override
    public PagedProductResponse getPagedProducts(int pageNumber, String keyword, Long categoryId, String orderBy) {
        PageRequest pageRequest = ProductAdapterUtil.createPageRequest(pageNumber, orderBy);

        Page<Product> productPage = categoryId != null
                ? productRepository.findByCategoryIdAndKeyword(categoryId, keyword, pageRequest)
                : productRepository.findByKeyword(keyword, pageRequest);

        List<ProductListResponse> products = ProductAdapterUtil.toProductListResponse(productPage.getContent());

        return new PagedProductResponse(
                products,
                productPage.getNumber(),
                productPage.hasPrevious(),
                productPage.hasNext()
        );
    }

    @Override
    public List<ProductListResponse> getAllProducts(String keyword, Long categoryId) {
        List<Product> products = (categoryId != null)
                ? productRepository.findAllByCategoryIdAndKeyword(categoryId, keyword)
                : productRepository.findAllByKeyword(keyword);

        return ProductAdapterUtil.toProductListResponse(products);
    }
}

package doit.shop.repository;

import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductListResponse;
import doit.shop.domain.Category;
import doit.shop.domain.CategoryType;
import doit.shop.domain.Product;
import doit.shop.port.product.ProductJpaAdapter;
import doit.shop.port.product.ProductPersistencePort;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductRepositoryPerformanceTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ProductPersistencePort productPersistencePort;

    private static final Random random = new Random();

    @BeforeAll
    static void setUp(@Autowired ProductRepository productRepository,
                      @Autowired CategoryRepository categoryRepository) {
        List<Category> categories = categoryRepository.findAll();

        // 카테고리가 없을 경우 초기화
        if (categories.isEmpty()) {
            categories = initializeCategories(categoryRepository);
        }

        createProducts(productRepository, categories, 100000); // 10만 개 데이터 생성
    }

    @BeforeEach
    void initPersistencePort() {
        // ProductJpaAdapter를 통해 PersistencePort 구현
        this.productPersistencePort = new ProductJpaAdapter(productRepository);
    }

    private static List<Category> initializeCategories(CategoryRepository categoryRepository) {
        List<Category> categories = new ArrayList<>();
        for (CategoryType type : CategoryType.values()) {
            categories.add(new Category(type));
        }
        categoryRepository.saveAll(categories);
        return categories;
    }

    @Test
    void testFindByCategoryIdAndKeywordPerformance() {
        // 특정 카테고리 가져오기
        Optional<Category> optionalCategory = categoryRepository.findByCategoryType(CategoryType.SNACKS);
        assertThat(optionalCategory).isPresent();
        Long categoryId = optionalCategory.get().getId();

        String keyword = "Product-1"; // 특정 키워드

        long startTime = System.currentTimeMillis();

        PageRequest pageRequest = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "price"));
        Page<Product> result = productRepository.findByCategoryIdAndKeyword(categoryId, keyword, pageRequest);

        long endTime = System.currentTimeMillis();
        logger.info("Execution time for testFindByCategoryIdAndKeywordPerformance: {} ms", endTime - startTime);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).allMatch(product -> product.getName().contains(keyword));
    }

    @Test
    void testFindAllByKeywordPerformance() {
        String keyword = "Product"; // 키워드

        long startTime = System.currentTimeMillis();

        PageRequest pageRequest = PageRequest.of(1, 50, Sort.by(Sort.Direction.ASC, "name"));
        Page<Product> result = productRepository.findByKeyword(keyword, pageRequest);

        long endTime = System.currentTimeMillis();
        logger.info("Execution time for testFindAllByKeywordPerformance: {} ms", endTime - startTime);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).allMatch(product -> product.getName().contains(keyword));
    }

    @Test
    void testGetAllProducts() {
        // 특정 카테고리 가져오기
        Optional<Category> optionalCategory = categoryRepository.findByCategoryType(CategoryType.SNACKS);
        assertThat(optionalCategory).isPresent();
        Long categoryId = optionalCategory.get().getId();

        String keyword = "Product-1"; // 특정 키워드

        long startTime = System.currentTimeMillis();

        // ProductPersistencePort의 getAllProducts 호출
        List<ProductListResponse> result = productPersistencePort.getAllProducts(keyword, categoryId);

        long endTime = System.currentTimeMillis();
        logger.info("Execution time for testGetAllProducts: {} ms", endTime - startTime);

        // 검증
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(product -> product.productName().contains(keyword));
        assertThat(result).allMatch(product -> product.categoryId().equals(categoryId));
    }

    @Test
    void comparePagedAndAllProducts() {
        // 특정 카테고리 가져오기
        Optional<Category> optionalCategory = categoryRepository.findByCategoryType(CategoryType.SNACKS);
        assertThat(optionalCategory).isPresent();
        Long categoryId = optionalCategory.get().getId();

        String keyword = "Product-1"; // 특정 키워드

        // 페이징된 제품 조회
        long pagedStartTime = System.currentTimeMillis();
        PagedProductResponse pagedResponse = productPersistencePort.getPagedProducts(0, keyword, categoryId, "price");
        long pagedEndTime = System.currentTimeMillis();
        logger.info("Execution time for getPagedProducts: {} ms", pagedEndTime - pagedStartTime);

        // 페이징 없는 제품 조회
        long allStartTime = System.currentTimeMillis();
        List<ProductListResponse> allProducts = productPersistencePort.getAllProducts(keyword, categoryId);
        long allEndTime = System.currentTimeMillis();
        logger.info("Execution time for getAllProducts: {} ms", allEndTime - allStartTime);

        // 검증
        assertThat(pagedResponse.result()).isNotEmpty();
        assertThat(pagedResponse.result()).allMatch(product -> product.productName().contains(keyword));
        assertThat(pagedResponse.result()).allMatch(product -> product.categoryId().equals(categoryId));

        assertThat(allProducts).isNotEmpty();
        assertThat(allProducts).allMatch(product -> product.productName().contains(keyword));
        assertThat(allProducts).allMatch(product -> product.categoryId().equals(categoryId));

        // 결과 수 비교
        logger.info("Paged result count: {}", pagedResponse.result().size());
        logger.info("All products count: {}", allProducts.size());

        assertThat(allProducts.size()).isGreaterThanOrEqualTo(pagedResponse.result().size());
    }

    // --- 데이터 생성 메서드 ---
    private static void createProducts(ProductRepository productRepository, List<Category> categories, int count) {
        List<Product> products = new ArrayList<>();
        long startTime = System.currentTimeMillis(); // 데이터 생성 시작 시간 측정
        int batchSize = 1000; // 배치 크기
        int totalBatches = count / batchSize;
        int remainingItems = count % batchSize;

        logger.info("Starting product data insertion. Total items: {}, Batch size: {}", count, batchSize);

        for (int i = 1; i <= count; i++) {
            Product product = Product.builder()
                    .name("Product-" + i) // Product-1, Product-2, ...
                    .description("Description of Product-" + i)
                    .price(random.nextInt(100000) + 1) // 랜덤 가격
                    .stock(random.nextInt(100) + 1) // 랜덤 재고
                    .image("https://example.com/images/product-" + i + ".jpg")
                    .category(categories.get(random.nextInt(categories.size()))) // 랜덤 카테고리
                    .build();
            products.add(product);

            // 배치 저장 처리
            if (products.size() % batchSize == 0) {
                long batchStartTime = System.currentTimeMillis();
                productRepository.saveAll(products); // 배치 저장
                products.clear();
                long batchEndTime = System.currentTimeMillis();
                logger.info("Inserted batch {}/{}. Batch execution time: {} ms", i / batchSize, totalBatches, batchEndTime - batchStartTime);
            }
        }

        // 남은 데이터 처리
        if (!products.isEmpty()) {
            long remainingStartTime = System.currentTimeMillis();
            productRepository.saveAll(products); // 남은 데이터 저장
            long remainingEndTime = System.currentTimeMillis();
            logger.info("Inserted remaining items: {}. Execution time: {} ms", remainingItems, remainingEndTime - remainingStartTime);
        }

        long endTime = System.currentTimeMillis();
        logger.info("Total execution time for inserting {} products: {} ms", count, endTime - startTime);
    }

}

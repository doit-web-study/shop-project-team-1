package doit.shop.controller.product;

import doit.shop.common.jwt.JwtProvider;
import doit.shop.controller.ListWrapper;
import doit.shop.controller.product.dto.*;
import doit.shop.controller.product.entity.Category;
import doit.shop.controller.product.entity.Product;
import doit.shop.controller.product.repository.CategoryRepository;
import doit.shop.controller.product.repository.ProductRepository;
import doit.shop.controller.user.UserRepository;
import doit.shop.controller.user.entity.UserEntity;
import doit.shop.exception.CustomException;
import doit.shop.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private JwtProvider jwtProvider;

    public ListWrapper<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> responses = categories.stream().map(c ->
                new CategoryResponse(c.getCategoryId(),c.getCategoryType())
        ).toList();

        return new ListWrapper<>(responses);
    }

    public ProductResponse uploadProduct(ProductUploadRequest request, HttpServletRequest httpRequest) {

        String accessToken = jwtProvider.resolveToken(httpRequest);
        String userId = jwtProvider.getUserId(accessToken);
        UserEntity user = userRepository.findByLoginId(userId);

        if(!jwtProvider.isValidToken(accessToken,userId))
            throw new CustomException(ErrorCode.NO_TOKEN_EXIST);

        Product product = Product.builder()
                .name(request.getProductName())
                .description(request.getProductDescription())
                .price(request.getProductPrice())
                .stock(request.getProductStock())
                .image(request.getProductImage())
                .categoryId(request.getCategoryId())
                .userId(user.getUserId())
                .build();

        productRepository.save(product);

        return new ProductResponse(product.getProductId());
    }

    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request, HttpServletRequest httpRequest) {

        Product product = checkUserAndProduct(productId, httpRequest);

        product.updateProduct(request.getProductName(),request.getProductDescription(),request.getProductPrice(),request.getProductStock(),request.getProductImage(),request.getCategoryId());
        productRepository.save(product);

        return new ProductResponse(product.getProductId());
    }

    public void delete(Long productId, HttpServletRequest httpRequest) {
        Product product = checkUserAndProduct(productId, httpRequest);

        productRepository.delete(product);
    }

    private Product checkUserAndProduct(Long productId, HttpServletRequest httpRequest) {
        String accessToken = jwtProvider.resolveToken(httpRequest);
        String userId = jwtProvider.getUserId(accessToken);

        UserEntity user = userRepository.findByLoginId(userId);
        Product product = productRepository.findByProductId(productId);
        if(product==null)
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);

        if(!user.getUserId().equals(product.getUserId()))
            throw new CustomException(ErrorCode.NOT_UPLOADER);

        return product;
    }

    public Page<Product> getMultiProducts(Pageable pageable, String keyword, Long categoryId) {

        if(categoryId!=null) {
            return productRepository.searchKeywordByCategoryId(pageable, keyword, categoryId);
        } else {
            return productRepository.searchKeyword(pageable, keyword);
        }

    }

    public ProductInfoResponse getProduct(Long categoryId) {
        Product product = productRepository.findByCategoryId(categoryId);
        if(product==null)
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);

        Category category = categoryRepository.findByCategoryId(categoryId);
        if(category==null)
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);

        UserEntity user = userRepository.findByUserId(product.getUserId());
        if(user==null)
            throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return ProductInfoResponse.builder()
                .productId(product.getProductId())
                .productDescription(product.getDescription())
                .productName(product.getName())
                .productStock(product.getStock())
                .productImage(product.getImage())
                .productPrice(product.getPrice())
                .categoryId(product.getCategoryId())
                .categoryType(category.getCategoryType())
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .userNickname(user.getNickname())
                .userId(user.getUserId())
                .build();
    }
}

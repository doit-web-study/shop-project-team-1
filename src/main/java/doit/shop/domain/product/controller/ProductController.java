package doit.shop.domain.product.controller;

import doit.shop.domain.product.dto.CategoryResponse;
import doit.shop.domain.product.dto.ProductInfoResponse;
import doit.shop.domain.product.dto.ProductUploadRequest;
import doit.shop.domain.product.dto.ProductUploadResponse;
import doit.shop.domain.product.entity.Category;
import doit.shop.domain.product.entity.Product;
import doit.shop.domain.product.service.CategoryService;
import doit.shop.domain.product.service.ProductService;
import doit.shop.domain.user.dto.UserLoginRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController implements ProductControllerDocs{

    @Autowired
    private final ProductService productService;
    private final CategoryService categoryService;

    // 상품 1개 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductInfoResponse> searchOne(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.searchOne(productId));
    }

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductUploadResponse> upload(@RequestBody ProductUploadRequest productUploadRequest) {
        return ResponseEntity.ok(productService.upload(productUploadRequest));
    }


    // 상품 여러개 한번에 조회
    @GetMapping("?page={pageId}&orderBy={orderBy}")
    public void searchMany(@PathVariable("pageId") Long pageId, @PathVariable("orderBy") String orderBy) {

    }

    // 상품 카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<Map<String, List<CategoryResponse>>> getCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();

        Map<String, List<CategoryResponse>> response = new HashMap<>();
        response.put("result", categories);

        return ResponseEntity.ok(response);
    }

}

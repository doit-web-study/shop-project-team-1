package doit.shop.controller.product;

import doit.shop.controller.product.dto.PagedProductResponse;
import doit.shop.controller.product.dto.ProductRequest;
import doit.shop.controller.product.dto.ProductResponse;
import doit.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> registerProduct(
            @RequestBody ProductRequest productRequest
    ) {
        // 상품 등록 처리
        ProductResponse productResponse = productService.registerProduct(productRequest, productRequest.productImage());
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping
    public ResponseEntity<PagedProductResponse> getProducts(
            @RequestParam int pageNumber,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "latest") String orderBy
    ) {
        PagedProductResponse response = productService.getProducts(pageNumber, keyword, categoryId, orderBy);
        return ResponseEntity.ok(response);
    }
}

package doit.shop.controller.product;

import doit.shop.controller.ListWrapper;
import doit.shop.controller.product.dto.*;
import doit.shop.controller.product.entity.Product;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/categories")
    public ListWrapper<CategoryResponse> getCategories(){
        return productService.getCategories();
    }

    @PostMapping("/")
    public ProductResponse uploadProduct(@RequestBody ProductUploadRequest request, HttpServletRequest httpRequest){
        return productService.uploadProduct(request, httpRequest);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id,@RequestBody ProductUpdateRequest request, HttpServletRequest httpRequest){
        return productService.updateProduct(id, request, httpRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id, HttpServletRequest httpRequest){
        productService.delete(id, httpRequest);
    }
    @GetMapping
    public Page<ProductInfoResponse> getMultiProducts(Pageable pageable, @RequestParam String keyword, @RequestParam Long categoryId){
        return productService.getMultiProducts(pageable,keyword,categoryId);
    }

    @GetMapping("/{id}")
    public ProductInfoResponse getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

}

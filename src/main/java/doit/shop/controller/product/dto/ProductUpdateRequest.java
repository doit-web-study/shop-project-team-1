package doit.shop.controller.product.dto;

import doit.shop.controller.product.entity.CategoryType;
import lombok.Getter;

@Getter
public class ProductUpdateRequest {
    private String productName;
    private String productDescription;
    private Integer productPrice;
    private Integer productStock;
    private String productImage;
    private Long categoryId;
}

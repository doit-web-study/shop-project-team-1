package doit.shop.controller.product.dto;

import doit.shop.controller.product.entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ProductInfoResponse {

    private Long productId;
    private String productName;
    private String productDescription;
    private Integer productPrice;
    private Integer productStock;
    private String productImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long categoryId;
    private CategoryType categoryType;
    private Long userId;
    private String userNickname;
}

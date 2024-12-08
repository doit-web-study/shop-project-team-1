package doit.shop.controller.product.dto;

import doit.shop.controller.product.entity.CategoryType;
import lombok.Getter;


@Getter
public class CategoryResponse {

    public CategoryResponse(Long categoryId, CategoryType categoryType) {
        this.categoryId = categoryId;
        this.categoryType = categoryType;
    }

    private Long categoryId;
    private CategoryType categoryType;
}

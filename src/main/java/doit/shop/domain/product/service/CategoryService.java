package doit.shop.domain.product.service;

import doit.shop.domain.product.dto.CategoryResponse;
import doit.shop.domain.product.entity.Category;
import doit.shop.domain.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // 모든 카테고리들 조회해서 리스트로 만들어서 반환
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryResponse(category.getId(), category.getType()))
                .collect(Collectors.toList());
    }

}

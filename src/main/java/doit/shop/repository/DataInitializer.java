package doit.shop.repository;

import doit.shop.domain.Category;
import doit.shop.domain.CategoryType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initCategories() {
        for (CategoryType type : CategoryType.values()) {
            categoryRepository.findByCategoryType(type)
                    .orElseGet(() -> categoryRepository.save(new Category(type)));
        }
    }
}

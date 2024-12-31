package doit.shop.domain.product.entity;

import doit.shop.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Integer price;
    private Integer stock;
    private String image;
    private Long categoryId;

    @Builder
    private Product(String name, String description, Integer price, Integer stock, String image, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.categoryId = categoryId;
    }

}

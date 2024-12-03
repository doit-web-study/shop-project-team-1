package doit.shop.controller.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;
    private CategoryType categoryType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

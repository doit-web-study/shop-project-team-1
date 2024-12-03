package doit.shop.controller.product.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String name;
    private String description;
    private String image;
    private Integer price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId", insertable = false, updatable = false)
    Long categoryId;
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    Long userId;

    @Builder
    public Product(String name, String description, String image, Integer price, Integer stock, Long categoryId, Long userId) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.createdAt = LocalDateTime.now();
        this.userId = userId;
    }

    public void updateProduct(String name, String description, Integer price, Integer stock, String image, Long categoryId) {
        if (name!=null)
            this.name = name;
        if (description!=null)
            this.description = description;
        if (image!=null)
            this.image = image;
        if (price!=null)
            this.price = price;
        if (stock!=null)
            this.stock = stock;
        if(categoryId!=null)
            this.categoryId = categoryId;
        this.modifiedAt = LocalDateTime.now();
    }
}
